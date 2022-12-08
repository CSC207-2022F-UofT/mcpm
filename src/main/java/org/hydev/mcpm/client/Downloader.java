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
                    // We use the size of 8096 here since it's typically the internal buffer size of an OS stream.
                    // This allows us to grab the entire buffer in one go and allow the OS to continue reading.
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
    @SuppressWarnings("unused")
    public Downloader threads(int threads)
    {
        this.threads = threads;
        return this;
    }
}
