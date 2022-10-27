package org.hydev.mcpm.client;

import java.io.File;
import java.util.Map;

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
        throw new UnsupportedOperationException("TODO");
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
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
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
