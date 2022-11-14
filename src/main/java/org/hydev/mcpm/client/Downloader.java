package org.hydev.mcpm.client;

import org.hydev.mcpm.client.interaction.ProgressBar;
import org.hydev.mcpm.client.interaction.ProgressBarTheme;
import org.hydev.mcpm.client.interaction.ProgressRow;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.net.URL;

import static java.lang.String.format;
import static org.hydev.mcpm.utils.GeneralUtils.safeSleep;

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


    /**
     * Download one file from the internet to local storage through HTTP request
     *
     * @param url Remote file URL
     * @param to Local file path
     */

    public void downloadFile(String url, File to)
    {
        // TODO: Implement this
        // throw new UnsupportedOperationException("TODO");
        try {
            URL link = new URL(url);
            HttpURLConnection http = (HttpURLConnection)link.openConnection();
            long fileSize = (long)http.getContentLengthLong();


            BufferedInputStream in = new BufferedInputStream(http.getInputStream());
            FileOutputStream fileos = new FileOutputStream(to);
            BufferedOutputStream bout = new BufferedOutputStream(fileos, 1024);
            byte[] buffer = new byte[1024];
            int read = 0;

            var b = new ProgressBar(ProgressBarTheme.ASCII_THEME);
            var all = new ArrayList<ProgressRow>();
            var row = new ProgressRow(fileSize)
                    .unit("MB")
                    .desc(format("Downloaded", all.size()))
                    .descLen(30);
            all.add(b.appendBar(row));
            while ((read = in.read(buffer, 0, 1024)) >= 0) {

                bout.write(buffer, 0, read);
                all.forEach(a -> a.increase(1024));
            }
            bout.close();
            in.close();
            System.out.println("Download completed");
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
     * @param progress Show progress or not
     * @param threads Number of simultaneous downloads
     */
    public void downloadFiles(Map<String, File> urls, boolean progress, int threads)
    {
        Thread[] threadss = new Thread[threads];
        final int UrlPerThread = urls.size() / threads;
        final int remainingUrl = urls.size() % threads;

        for (int t = 0; t < threads; t++) {
            final int thread = t;
            threadss[t] = new Thread() {
                @Override
                public void run() {
                    runThread(urls, threads, thread, UrlPerThread, remainingUrl);
                }
            };
        }

        for (Thread t: threadss) {
            t.start();
        }
    }
    private void runThread(Map<String, File> urls, int threads, int thread, int UrlPerThread, int remainingUrl) {
        // Store all urls into a list
        List<String> files = new ArrayList<>();
        for (Map.Entry<String, File> url:urls.entrySet()) {
            files.add(url.getKey());
        }
        // Store urls per thread
        List<String> inFiles = new ArrayList<>();

        for (int i=thread*UrlPerThread; i < (thread + 1) * UrlPerThread; i++) {
            inFiles.add(files.get(i));
        }
        if (thread == threads-1 && remainingUrl > 0) {
            for (int j = files.size() - remainingUrl; j < files.size(); j++) {
                inFiles.add(files.get(j));
            }
        }

        for (String url:inFiles) {
            downloadFile(url, urls.get(url));
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