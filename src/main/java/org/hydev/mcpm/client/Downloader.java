package org.hydev.mcpm.client;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.hydev.mcpm.client.display.progress.ProgressBar;
import org.hydev.mcpm.client.display.progress.ProgressBarTheme;
import org.hydev.mcpm.client.display.progress.ProgressRow;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * File downloader
 */
public class Downloader
{
    /** Number of simultaneous downloads */
    private int threads = 5;

    private final ProgressBar bar = new ProgressBar(ProgressBarTheme.ASCII_THEME);

    /**
     * Download one file from the internet to local storage through HTTP request
     *
     * @param url Remote file URL
     * @param to Local file path
     */
    public void downloadFile(String url, String to)
    {
        try (var client = HttpClients.createDefault())
        {
            File fos = new File(to);
            var get = new HttpGet(url);
            var bytes = client.execute(get, (req) ->
            {
                HttpEntity entity = req.getEntity();
                var total = entity.getContentLength();
                var builder = new ByteArrayOutputStream((int) total);

                // Create progress row
                var row = new ProgressRow(total).desc(fos.getName()).descLen(30);
                bar.appendBar(row);

                try (var stream = entity.getContent())
                {
                    var buffer = new byte[8096];

                    var count = stream.read(buffer);
                    while (count > 0)
                    {
                        builder.write(buffer, 0, count);
                        row.increase(count);
                        count = stream.read(buffer);
                    }
                }

                return builder.toByteArray();
            });
            try {
                Files.write(fos.toPath(), bytes);
            }
            catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Download multiple files from the internet to local storage through HTTP requests
     * <p>
     * The implementation must use multithreading
     *
     * @param urls Mapping of remote urls to local file paths
     */
    public void downloadFiles(Map<String, String> urls)
    {
        try (ExecutorService executor = Executors.newFixedThreadPool(threads))
        {
            var files = urls.keySet().stream().toList();

            if (files.size() > 0)
            {
                for (String url : files)
                {
                    executor.submit(() ->
                    {
                        String to = urls.get(url);
                        downloadFile(url, to);
                    });
                }
                executor.shutdown();
                // All files submitted for downloading

                //noinspection ResultOfMethodCallIgnored
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

                // All files are completely downloaded
            }
        } catch (InterruptedException e) { /* do nothing */ }
    }

    /**
     * Setter for threads
     *
     * @param threads Number of simultaneous downloads
     * @return this (for fluent access)
     */
    public Downloader threads(int threads)
    {
        this.threads = threads;
        return this;
    }

    /**
     * Displays a demo for downloader.
     *
     * @param args Not used
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args)
    {
        // Remember to chang link to test
        String link = "https://sd.blackball.lv/library/Introduction_to_Algorithms_Third_Edition_(2009).pdf";
        String out = "./Introduction_to_Algorithms_Third_Edition.pdf";
        String link1 = "https://www.iusb.edu/students/academic-success-programs/academic-centers-for-excellence/docs/Basic%20Math%20Review%20Card.pdf";
        String out1 = "./Math.pdf";
        String link2 = "https://ouopentextbooks.org/mathematics/files/2015/07/1503.pdf";
        String out2 = "./1503";
        String link3 = "https://faculty.math.illinois.edu/~aydin/math220/lecturenotes/m220_Sec1_4.pdf";
        String out3 = "./m220_Sec1_4";
        String link4 = "https://ocw.mit.edu/ans7870/9/9.00SC/MIT9_00SCF11_text.pdf";
        String out4 = "./MIT9_00SCF11_text";

        final var downloader = new Downloader();

        Map<String, String> urls = new HashMap<>();
        urls.put(link, out);
        urls.put(link1, out1);
        urls.put(link2, out2);
        urls.put(link3, out3);
        urls.put(link4, out4);
        downloader.downloadFiles(urls);
        File outFile = new File(out);
        outFile.delete();
        File outFile1 = new File(out1);
        outFile1.delete();
        File outFile2 = new File(out2);
        outFile2.delete();
        File outFile3 = new File(out3);
        outFile3.delete();
        File outFile4 = new File(out4);
        outFile4.delete();
    }
}
