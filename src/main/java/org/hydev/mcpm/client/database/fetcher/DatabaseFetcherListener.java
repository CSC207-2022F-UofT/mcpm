package org.hydev.mcpm.client.database.fetcher;

public interface DatabaseFetcherListener {
    void download(long completed, long total);
    void finish();
}
