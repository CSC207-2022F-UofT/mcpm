package org.hydev.mcpm.server.crawlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.fluent.Request;
import org.hydev.mcpm.server.crawlers.spiget.SpigetResource;
import org.hydev.mcpm.server.crawlers.spiget.SpigetVersion;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static java.lang.String.format;
import static org.hydev.mcpm.Utils.makeUrl;
import static org.hydev.mcpm.Utils.safeSleep;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class SpigetCrawler
{
    public static final String SPIGET = "https://api.spiget.org/v2";
    public static final String UA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36";
    public static final ObjectMapper JACKSON = new ObjectMapper()
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false).enable(INDENT_OUTPUT);;
    public static final long MT_DELAY = 1000;

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
            var resp = Request.get(makeUrl(SPIGET + "/resources", "size", 500, "sort", "+id", "page", i,
                    "fields", "id,name,tag,external,likes,testedVersions,links,contributors,premium,price,currency,version,releaseDate,updateDate,downloads,existenceStatus"))
                .addHeader("User-Agent", UA).execute().returnContent().asString();

            // Parse JSON
            ArrayNode page = (ArrayNode) JACKSON.readTree(resp);

            // Print debug info
            if (!page.isEmpty())
                System.out.printf("Page %s done, page len: %s, last id: %s\n", i, page.size(), page.get(page.size() - 1).get("id").asLong());
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
     * Crawl a list of all resources (cached to .mcpm/crawler/spiget/resources.json)
     *
     * @param refresh Ignore caches
     * @return List of all resources, or an empty list on error
     */
    public List<SpigetResource> crawlAllResources(boolean refresh)
    {
        var time = System.currentTimeMillis();
        var outPath = new File(".mcpm/crawler/spiget/resources.json");
        var bakPath = new File(".mcpm/crawler/spiget/backups/resources." + time + ".json");
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
     * Crawl the versions of a resource
     *
     * @param resourceId Resource ID
     * @param refresh Whether to invalidate and refresh cache
     * @return List of versions
     */
    public List<SpigetVersion> crawlVersions(long resourceId, boolean refresh)
    {
        var outPath = new File(".mcpm/crawler/spiget/versions/" + resourceId + ".json");

        try
        {
            // Read existing cache
            if (outPath.isFile() && !refresh)
                return Arrays.asList(JACKSON.readValue(outPath, SpigetVersion[].class));

            // Obtain new version info from HTTP request
            var resp = Request.get(makeUrl(SPIGET + "/resources/" + resourceId + "/versions", "size", 500))
                .addHeader("User-Agent", UA).execute().returnContent().asString();

            // Parse json (validate that it can be parsed to json)
            var json = Arrays.asList(JACKSON.readValue(resp, SpigetVersion[].class));

            // Write to cache
            outPath.getParentFile().mkdirs();
            Files.writeString(outPath.toPath(), resp);

            System.out.printf("Versions obtained for resource %s\n", resourceId);

            safeSleep(150);

            return json;
        }
        catch (IOException e)
        {
            if (e.getMessage().contains("404")) return new ArrayList<>();
            throw new RuntimeException(e);
        }
    }

    /**
     * Filter out duplicate versions (If there are multiple versions with the same name, choose
     * the one that's uploaded last)
     *
     * @param versions List of versions
     * @return List of versions without duplicates
     */
    private List<SpigetVersion> filterDuplicateVersions(List<SpigetVersion> versions)
    {
        // Use linked hash map to preserve order
        var tmp = new LinkedHashMap<String, SpigetVersion>();

        // Sort so that the newest release is at the last, then put them in a map in order
        // Then the map values will be the newest release of each version
        versions.stream().sorted((a, b) -> Long.compare(b.releaseDate(), a.releaseDate()))
            .forEach(it -> tmp.put(it.name(), it));

        return tmp.values().stream().toList();
    }

    /**
     * Get file download path for a version
     *
     * @param version Version of a plugin
     * @return File download path
     */
    private File getTemporaryDownloadPath(SpigetVersion version)
    {
        return new File(format(".mcpm/crawler/spiget/dl-cache/%s/%s",
            version.resource(), version.name()));
    }

    /**
     * Get file download path for the latest version of a plugin
     *
     * @param res Plugin
     * @return File download path
     */
    private File getTemporaryDownloadPath(SpigetResource res)
    {
        return new File(format(".mcpm/crawler/spiget/dl-cache/latest/%s.jar",
            res.id()));
    }

    /**
     * Get a list of versions in all plugins that haven't been downloaded locally
     *
     * @param res List of plugins
     * @return List of versions
     */
    private List<SpigetVersion> getVersionsToDownload(List<SpigetResource> res)
    {
        return res.stream().flatMap(r -> filterDuplicateVersions(crawlVersions(r.id(), false))
            .stream().filter(v -> !getTemporaryDownloadPath(v).isFile()))
            .collect(Collectors.toList());
    }

    private void download(SpigetVersion version)
    {
        var fp = getTemporaryDownloadPath(version);

        // Make request
        var url = makeUrl(format(SPIGET + "/resources/%s/versions/%s/download",
            version.resource(), version.id()));

        try
        {
            // Write bytes
            Files.write(fp.toPath(), Request.get(url).addHeader("User-Agent", UA).execute()
                .returnContent().asBytes());

            System.out.printf("Downloaded %s version %s", version.url(), version.name());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Downlaod latest version of a plugin if not present
     *
     * @param res Resource
     */
    private void downloadLatest(SpigetResource res)
    {
        var fp = getTemporaryDownloadPath(res);
        if (fp.isFile() || res.external()) return;

        // Make request
        var url = makeUrl(format(SPIGET + "/resources/%s/download", res.id()));

        try
        {
            // Write bytes
            fp.getParentFile().mkdirs();
            Files.write(fp.toPath(), Request.get(url).addHeader("User-Agent", UA).execute()
                .returnContent().asBytes());

            System.out.printf("Downloaded (%s) %s latest version jar\n", res.id(), res.name());
        }
        catch (HttpResponseException e)
        {
            // Not found
            if (e.getMessage().contains("404")) return;
            // "External resource cannot be downloaded"
            if (e.getMessage().contains("400")) return;
            // Blocked by cloudflare
            if (e.getMessage().contains("520")) return;
            // This happens when the server has an error (e.g. when a plugin doesn't have files to download)
            if (e.getMessage().contains("502")) return;
            System.out.println("Error when downloading " + url + " " + e.getMessage());
            //throw new RuntimeException("Error when downloading " + url, e);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            //throw new RuntimeException("Error when downloading " + url, e);
            System.out.println("Error when downloading " + url + " " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        var crawler = new SpigetCrawler();
        var res = crawler.crawlAllResources(false).stream()
            .filter(it -> it.downloads() > 1000 && !it.external()).toList();

        System.out.println(res.size());

        // .parallel()
        //res.stream().filter(it -> it.downloads() > 1000).map(SpigetResource::id).map(it -> {
        //    crawler.crawlVersions(it, false);
        //
        //    // Wait some time (because of rate limit)
        //    //safeSleep(MT_DELAY);
        //
        //    return 0;
        //}).toList();

        //crawler.getVersionsToDownload(res).stream().forEach(crawler::download);

        res.stream().filter(it -> !crawler.getTemporaryDownloadPath(it).isFile()).parallel().forEach(it ->
        {
            crawler.downloadLatest(it);

            safeSleep(MT_DELAY);
        });

        //new SpigetCrawler().crawlAllResources();
        //new SpigetCrawler().crawlVersions(2, true);
    }
}
