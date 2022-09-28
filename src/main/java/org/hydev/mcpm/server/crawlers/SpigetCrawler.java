package org.hydev.mcpm.server.crawlers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.hc.client5.http.fluent.Request;
import org.hydev.mcpm.server.crawlers.spiget.SpigetResource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.hydev.mcpm.Utils.makeUrl;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class SpigetCrawler
{
    public static final String SPIGET = "https://api.spiget.org/v2";
    public static final ObjectMapper JACKSON = new ObjectMapper()
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

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
            var resp = Request.get(makeUrl(SPIGET + "/resources", "size", 500, "sort", "+id", "page", i))
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36")
                .execute().returnContent().asString();

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
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Crawl a list of all resources (cached to .mcpm/crawler/spiget/resources.json)
     *
     * @return List of all resources, or an empty list on error
     */
    public List<SpigetResource> crawlAllResources()
    {
        var time = System.currentTimeMillis();
        var outPath = new File(".mcpm/crawler/spiget/resources.json");
        var bakPath = new File(".mcpm/crawler/spiget/backups/resources." + time + ".json");
        long perChunk = 20;

        try
        {
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

    public static void main(String[] args)
    {
        new SpigetCrawler().crawlAllResources();
    }
}
