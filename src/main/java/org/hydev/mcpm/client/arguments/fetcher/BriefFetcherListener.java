package org.hydev.mcpm.client.arguments.fetcher;

/**
 * Stub implementation of DatabaseFetcherListener.
 * When the download method is called for the first time, the user will be alerted.
 */
public class BriefFetcherListener implements DatabaseFetcherListener {
    private boolean startedDownload = false;

    @Override
    public void download(long completed, long total) {
        if (!startedDownload) {
            // Should be passed up to a parent class.
            System.out.println("Downloading database...");

            startedDownload = true;
        }
    }

    @Override
    public void finish() {
        /* nothing */
    }
}
