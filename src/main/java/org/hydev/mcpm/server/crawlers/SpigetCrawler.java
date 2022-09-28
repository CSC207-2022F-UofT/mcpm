package org.hydev.mcpm.server.crawlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.apache.hc.client5.http.fluent.Request;
import org.hydev.mcpm.server.crawlers.spiget.SpigetResource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    /**
     * Crawl a list of all resources (cached to .mcpm/crawler/spiget/resources.json)
     *
     * @return List of all resources, or an empty list on error
     */
    public List<SpigetResource> crawlAllResources()
    {
        var outPath = new File(".mcpm/crawler/spiget/resources.json");
        var pageCount = new File(".mcpm/crawler/spiget/page-count.txt");
        var jackson = new ObjectMapper();

        try
        {
            var lst = jackson.createArrayNode();
            long i = 1;

            // There exists previously crawled resources, continue from there.
            if (outPath.isFile() && pageCount.isFile())
            {
                lst = (ArrayNode) jackson.readTree(outPath);
                i = Long.parseLong(Files.readString(pageCount.toPath()).strip());
            }

            // Each page
            while (true)
            {
                // Send request
                var resp = Request.get(makeUrl(SPIGET + "/resources", "size", 100, "sort", "+id", "page", i))
                    .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36")
                    .execute().returnContent().asString();

                // Parse JSON
                var page = (ArrayNode) jackson.readTree(resp);
                lst.addAll(page);

                // Empty page, we're done
                if (page.isEmpty()) break;

                // Print debug info
                System.out.printf("Page %s done, total %s, last id %s\n",
                    i, lst.size(), lst.get(lst.size() - 1).get("id").asLong());

                // Write page to json
                outPath.getParentFile().mkdirs();
                jackson.writeValue(outPath, lst);
                Files.writeString(pageCount.toPath(), i + "");

                // Next page
                i += 1;
            }

            return Arrays.asList(jackson.treeToValue(lst, SpigetResource[].class));
        }
        catch (URISyntaxException e)
        {
            // Would never happen
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static void main(String[] args)
    {
        System.out.println(new SpigetCrawler().crawlAllResources());
    }
}
