package org.hydev.mcpm.client.database.fetcher;

/**
 * Stub implementation of DatabaseFetcherListener.
 * When the download method is called for the first time, the user will be alerted.
 */
public class BriefFetcherListener implements DatabaseFetcherListener {
    private boolean startedDownload = false;
    private boolean quiet = false;

    public BriefFetcherListener(boolean quiet) {
        this.quiet = quiet;
    }

    public BriefFetcherListener() {
        this(false);
    }

    @Override
    public void download(long completed, long total) {
        if (!startedDownload) {
            // Should be passed up to a parent class.
            if (!quiet) {
                System.out.println("Downloading database...");
            }

            startedDownload = true;
        }
    }

    @Override
    public void finish() {
        /* nothing */
    }
}
