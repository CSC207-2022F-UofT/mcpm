package org.hydev.mcpm.client.database.fetcher;

/**
 * Receives events on database download progress for DatabaseFetcher classes.
 */
public interface DatabaseFetcherListener {
    /**
     * Called to update the client on download progress when it receives additional bytes.
     * In a successful download, this method should be called at least once where completed == total.
     *
     * @param completed The amount of database bytes received up to this point.
     * @param total The amount of total bytes that must be downloaded.
     */
    void download(long completed, long total);

    /**
     * Called when download has finished or aborted so the client can clean up.
     */
    void finish();
}
