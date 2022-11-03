package org.hydev.mcpm.client.database.fetcher;

public class BriefFetcherListener implements DatabaseFetcherListener {
    boolean startedDownload = false;

    @Override
    public void download(long completed, long total) {
        if (!startedDownload) {
            // Should be passed up to a parent class.
            System.out.println("Downloading database...");

            startedDownload = true;
        }
    }

    @Override
    public void finish() { /* nothing */ }
}
