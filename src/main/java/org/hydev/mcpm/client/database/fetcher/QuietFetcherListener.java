package org.hydev.mcpm.client.database.fetcher;

/**
 * Stub implementation of DatabaseFetcherListener.
 * When the download method is called for the first time, the user will be alerted.
 */
public class QuietFetcherListener implements DatabaseFetcherListener {
    @Override
    public void download(long completed, long total) {
        /* nothing */
    }

    @Override
    public void finish() {
        /* nothing */
    }
}
