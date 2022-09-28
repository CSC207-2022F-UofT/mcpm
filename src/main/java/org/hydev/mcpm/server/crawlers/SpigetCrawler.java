package org.hydev.mcpm.server.crawlers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.hc.client5.http.fluent.Request;
import org.hydev.mcpm.server.crawlers.spiget.SpigetResource;
import org.hydev.mcpm.server.crawlers.spiget.SpigetVersion;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
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
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
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

    public static void main(String[] args)
    {
        var crawler = new SpigetCrawler();
        var res = crawler.crawlAllResources(false);

        // .parallel()
        res.stream().filter(it -> it.downloads() > 1000).map(SpigetResource::id).map(it -> {
            crawler.crawlVersions(it, false);

            // Wait some time (because of rate limit)
            //safeSleep(MT_DELAY);

            return 0;
        }).toList();

        //new SpigetCrawler().crawlAllResources();
        //new SpigetCrawler().crawlVersions(2, true);
    }
}
