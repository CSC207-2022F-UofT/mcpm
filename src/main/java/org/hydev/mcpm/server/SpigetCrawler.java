package org.hydev.mcpm.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.fluent.Request;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.server.spiget.SpigetResource;
import org.hydev.mcpm.utils.PluginJarFile;
import org.hydev.mcpm.utils.StoredHashMap;
import org.hydev.mcpm.utils.TemporaryDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static java.nio.file.Files.createSymbolicLink;
import static org.hydev.mcpm.Constants.JACKSON;
import static org.hydev.mcpm.server.spiget.CreateDatabase.packageStore;
import static org.hydev.mcpm.server.spiget.CreateDatabase.writeDatabase;
import static org.hydev.mcpm.utils.GeneralUtils.makeUrl;

/**
 * This class makes requests to spiget and gets new information on latest plugin updates.
 * It then organizes the data into a directory structure that can be inspected and served over HTTP.
 */
public class SpigetCrawler
{
    private final String spiget = "https://api.spiget.org/v2";
    private final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36";
    private final File dataDir;
    private final StoredHashMap<String, String> blacklist;

    public SpigetCrawler(File dataDir)
    {
        this.dataDir = dataDir;
        this.blacklist = new StoredHashMap<>(new File(dataDir, "crawler/spiget/blacklist.json"));
    }

    /**
     * Crawl one page of resources
     *
     * @param i Page number
     * @return List of resources
     */
    private List<JsonNode> crawlResourcesPage(long i)
    {
        try
        {
            // Send request
            var resp = Request.get(makeUrl(spiget + "/resources", "size", 500, "sort", "+id", "page", i,
                    "fields", "id,name,tag,external,likes,testedVersions,links,contributors,premium," +
                        "price,currency,version,releaseDate,updateDate,downloads,existenceStatus"))
                .addHeader("User-Agent", userAgent).execute().returnContent().asString();

            // Parse JSON
            ArrayNode page = (ArrayNode) JACKSON.readTree(resp);

            // Print debug info
            if (!page.isEmpty())
                System.out.printf("Page %s done, page len: %s, last id: %s\n",
                    i, page.size(), page.get(page.size() - 1).get("id").asLong());
            else
                System.out.printf("Page %s is empty\n", i);

            // Return array
            return StreamSupport.stream(page.spliterator(), false).toList();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crawl a list of all resources (cached to dataDir/crawler/spiget/resources.json)
     *
     * @param refresh Ignore caches
     * @return List of all resources, or an empty list on error
     */
    public List<SpigetResource> crawlAllResources(boolean refresh)
    {
        var time = System.currentTimeMillis();
        var outPath = new File(dataDir, "crawler/spiget/resources.json");
        var bakPath = new File(dataDir, "crawler/spiget/backups/resources." + time + ".json");
        long perChunk = 20;

        try
        {
            // Read from existing cache
            if (outPath.isFile() && !refresh) return Arrays.asList(JACKSON.readValue(outPath, SpigetResource[].class));

            var lst = JACKSON.createArrayNode();
            long i = 1;

            // Each page
            while (true)
            {
                // Obtain one chunk with parallel processing
                var pages = LongStream.range(i, i + perChunk).parallel().mapToObj(this::crawlResourcesPage).toList();
                var page = pages.stream().flatMap(Collection::stream).toList();

                lst.addAll(page);

                // At least one page in the chunk is empty, we're done
                if (pages.stream().anyMatch(List::isEmpty)) break;

                // Print debug info
                System.out.printf("> Pages %s to %s done, total %s, last id %s\n",
                    i, i + perChunk, lst.size(), lst.get(lst.size() - 1).get("id").asLong());

                // Next page
                i += perChunk;
            }

            // There exists previously crawled resources, backup before saving.
            if (outPath.isFile())
            {
                if (bakPath.isFile()) bakPath.delete();
                bakPath.getParentFile().mkdirs();
                outPath.renameTo(bakPath);
            }
            JACKSON.writeValue(outPath, lst);

            return Arrays.asList(JACKSON.treeToValue(lst, SpigetResource[].class));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Get file download path for the latest version of a plugin
     *
     * @param res Plugin
     * @return File download path
     */
    private File getLatestPath(SpigetResource res)
    {
        return new File(dataDir, format("pkgs/spiget/%s/%s/release.jar", res.id(), res.version().id()));
    }

    /**
     * Check update for a plugin
     *
     * @param res Resource
     */
    private void checkUpdate(SpigetResource res)
    {
        // Plugin is in the blacklist, skip
        if (blacklist.containsKey("" + res.id())) return;

        // Latest version already exists in local fs, skip
        var fp = getLatestPath(res);
        if (fp.isFile()) return;

        // Resource is marked as external, we can't download it
        if (res.external()) return;

        // Try downloading the file
        System.out.printf("Trying to download %s: %s\n", res.id(), res.name());
        var url = makeUrl(format(spiget + "/resources/%s/download", res.id()));

        try (var tmp = new TemporaryDir())
        {
            var tmpFile = new File(tmp.path, "tmp.jar");

            // Write bytes
            // TODO: Maybe we can do this without tmp files, like read zip file content from byte array
            var jarBytes = Request.get(url).addHeader("User-Agent", userAgent).execute().returnContent().asBytes();
            Files.write(tmpFile.toPath(), jarBytes);

            // Try to read plugin.yml from it
            String metaStr;
            try (var plugin = new PluginJarFile(tmpFile))
            {
                metaStr = plugin.readString("plugin.yml");
            }
            catch (Exception e)
            {
                // Cannot read plugin.yml, that means it's not a standard plugin, add to blacklist
                System.out.printf("Cannot read plugin.yml (%s: %s)\n", res.id(), res.name());
                blacklist.put("" + res.id(), "Cannot read plugin.yml");
                return;
            }

            // Success, write to file
            fp.getParentFile().mkdirs();

            // Write meta to plugin.yml
            Files.writeString(new File(fp.getParentFile(), "plugin.yml").toPath(), metaStr);

            // Write jar to release.jar
            Files.write(fp.toPath(), jarBytes);

            System.out.printf("Downloaded (%s) %s latest version jar\n", res.id(), res.name());
        }
        catch (HttpResponseException e)
        {
            // Not found
            if (e.getMessage().contains("404"))
                blacklist.put("" + res.id(), "HTTP 404: Not found");

            // "External resource cannot be downloaded"
            else if (e.getMessage().contains("400"))
                blacklist.put("" + res.id(), "HTTP 400: Probably external resource");

            // Blocked by cloudflare
            else if (e.getMessage().contains("520"))
                blacklist.put("" + res.id(), "HTTP 520: External site, blocked by CloudFlare");

            // This happens when the server has an error (e.g. when a plugin doesn't have files to download)
            //else if (e.getMessage().contains("502")) return;

            System.out.println("Error when downloading " + url + " " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("Error when downloading " + url + " " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create symbolic links for a downloaded resource
     */
    public void links()
    {
        // Loop through each resource
        var resourcesPath = new File(dataDir, "pkgs/spiget");
        var files = resourcesPath.listFiles();
        if (files == null) return;

        AtomicInteger errors = new AtomicInteger();

        Arrays.stream(files).forEach(res -> {
            // Loop through each version
            var versions = res.listFiles();
            if (versions == null) return;

            Arrays.stream(versions).forEach(ver -> {
                // Read plugin.yml
                try
                {
                    var meta = PluginYml.fromYml(Files.readString(new File(ver, "plugin.yml").toPath()));

                    // Compute link path
                    var linkPath = new File(dataDir, format("pkgs/links/%s/%s", meta.name(), meta.version()));

                    // Delete old link
                    if (Files.isSymbolicLink(linkPath.toPath()) || linkPath.exists()) linkPath.delete();

                    // Create new link
                    linkPath.getParentFile().mkdirs();
                    createSymbolicLink(linkPath.toPath(), linkPath.getParentFile().toPath().relativize(ver.toPath()));
                }
                catch (IOException | NullPointerException | PluginYml.InvalidPluginMetaStructure e)
                {
                    System.err.printf("Cannot read plugin.yml for %s: %s\n", ver, e);
                    errors.getAndIncrement();
                }
            });
        });

        System.out.println("Errors: " + errors.get());
    }

    /**
     * Execute server crawler
     *
     * @param args Arguments (Unused)
     */
    public static void main(String[] args)
    {
        var crawler = new SpigetCrawler(new File(".mcpm"));
        var res = crawler.crawlAllResources(true).stream()
            .filter(it -> it.downloads() > 100 && !it.external()).toList();

        System.out.println(res.size());

        // TODO: Parallelize this. Currently causes ConcurrentModificationException with StoredHashMap
        //safeSleep(crawler.mtDelay);
        res.stream().filter(it -> !crawler.getLatestPath(it).isFile()).forEach(crawler::checkUpdate);

        // Create links
        crawler.links();

        // Create database
        writeDatabase(
            new File(packageStore, "pkgs/spiget"),
            new File(packageStore, "db"),
            new File(packageStore, "db.hash"),
            new File(packageStore, "db.timestamp")
        );
    }
}
