package org.hydev.mcpm.client;

import org.hydev.mcpm.client.interaction.ProgressBar;
import org.hydev.mcpm.client.interaction.ProgressBarTheme;
import org.hydev.mcpm.client.interaction.ProgressRow;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.String.format;

/**
 * File downloader
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class Downloader
{
    /** Whether to show progress bar during download */
    private boolean showProgress = false;

    /** Number of simultaneous downloads */
    private int threads = 5;

    private ProgressBar bar = new ProgressBar(ProgressBarTheme.ASCII_THEME);

    private ArrayList<ProgressRow> allRows = new ArrayList<>();

    /**
     * Download one file from the internet to local storage through HTTP request
     *
     * @param url Remote file URL
     * @param to Local file path
     */
    public void downloadFile(String url, File to)
    {
        try (FileOutputStream fileos = new FileOutputStream(to)) {
            URL link = new URL(url);
            HttpURLConnection http = (HttpURLConnection) link.openConnection();
            long fileSize = (long) http.getContentLengthLong();


            BufferedInputStream in = new BufferedInputStream(http.getInputStream());
            BufferedOutputStream bout = new BufferedOutputStream(fileos, 1024);
            byte[] buffer = new byte[1024];
            int read;

            var row = new ProgressRow(fileSize)
                    .desc(format("Download %s", allRows.size()))
                    .descLen(30);
            bar.appendBar(row);
            allRows.add(row);
            while ((read = in.read(buffer, 0, 1024)) >= 0) {
                bout.write(buffer, 0, read);
                allRows.forEach(a -> a.increase(1024));
            }
            bout.close();
            in.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Download multiple files from the internet to local storage through HTTP requests
     * <p>
     * The implementation must use multithreading
     *
     * @param urls Mapping of remote urls to local file paths
     */
    public void downloadFiles(Map<String, File> urls)
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
                        File to = urls.get(url);
                        downloadFile(url, to);
                    });
                }
                executor.shutdown();
                // All files submitted for downloading

                while (!executor.isTerminated()) {}
                // All files are completely downloaded
            }
        }
    }

    /**
     * Setter for showProgress
     *
     * @param showProgress Whether to show progress bar during download
     * @return this (for fluent access)
     */
    public Downloader showProgress(boolean showProgress)
    {
        this.showProgress = showProgress;
        return this;
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
}
