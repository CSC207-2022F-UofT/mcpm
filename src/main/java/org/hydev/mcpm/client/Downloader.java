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

    private final ProgressBar bar = new ProgressBar(ProgressBarTheme.ASCII_THEME);

    private final ArrayList<ProgressRow> allRows = new ArrayList<>();

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
            long fileSize = http.getContentLengthLong();


            BufferedInputStream in = new BufferedInputStream(http.getInputStream());
            BufferedOutputStream bout = new BufferedOutputStream(fileos, 1024);
            byte[] buffer = new byte[1024];
            int read;

            var row = new ProgressRow(fileSize)
                    .unit("Byte")
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
            if (files.size() > 1)
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
        downloader.downloadFiles(urls);
    }
}
