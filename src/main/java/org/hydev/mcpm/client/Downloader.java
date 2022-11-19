package org.hydev.mcpm.client;

import org.hydev.mcpm.client.interaction.ProgressBar;
import org.hydev.mcpm.client.interaction.ProgressBarTheme;
import org.hydev.mcpm.client.interaction.ProgressRow;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    private ArrayList<ProgressRow> allRows = new ArrayList<ProgressRow>();



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
            int read = 0;

            var row = new ProgressRow(fileSize)
                    .unit("Byte")
                    .desc(format("Downloaded", allRows.size()))
                    .descLen(30);
            bar.appendBar(row);
            allRows.add(row);
            while ((read = in.read(buffer, 0, 1024)) >= 0) {
                bout.write(buffer, 0, read);
                allRows.forEach(a -> a.increase(1024));
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
    public void downloadFiles(Map<String, File> urls, boolean progress, int threads) throws IOException {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        var files = urls.keySet().stream().toList();
        if (files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {
                executor.submit(new Processor(i, urls, files));
            }
            executor.shutdown();
            // All files submitted for downloading

            while (!executor.isTerminated()) {}
            // All files are completely downloaded
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

    class Processor implements Runnable {

        private List<String> files;
        private Map<String, File> urls;
        private int id;

        /**
         * Download multiple files from the internet to local storage through HTTP requests
         * <p>
         * The implementation must use multithreading
         *
         * @param files List of remote urls
         * @param urls Mapping of remote urls to local file paths
         * @param id id for each file
         */
        public Processor(int id, Map<String, File> urls, List<String> files) {
            this.id = id;
            this.urls = urls;
            this.files = files;
        }

        // Process downloading each file from multithreading
        @Override
        public void run() {
            String url = files.get(id);
            File to = urls.get(url);
            downloadFile(url, to);
        }
    }


    /**
     * Displays a demo for.
     *
     * @param args Arguments are ignored.
     */
    public static void main(String[] args) throws IOException {
        // Remember to chang link to test
        String link = "https://sd.blackball.lv/library/Introduction_to_Algorithms_Third_Edition_(2009).pdf";
        File out = new File("./Introduction_to_Algorithms_Third_Edition.pdf");
        String link1 = "https://www.iusb.edu/students/academic-success-programs/academic-centers-for-excellence/docs/Basic%20Math%20Review%20Card.pdf";
        File out1 = new File("./Math.pdf");
        Downloader downloader = new Downloader();
        Map<String, File> urls = new HashMap<>();
        urls.put(link, out);
        urls.put(link1, out1);
        downloader.downloadFiles(urls, true, 2);
    }
}