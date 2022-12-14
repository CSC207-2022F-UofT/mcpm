package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.database.mirrors.MirrorSelectBoundary;

import java.io.IOException;

/**
 * Controller for the refresh command
 */
public record RefreshController(
    DatabaseFetcher fetcher,
    DatabaseFetcherListener listener,
    MirrorSelectBoundary mirror
)
{
    /**
     * Refresh the database cache and mirror list
     */
    public void refresh() throws IOException
    {
        // Refresh local database
        if (fetcher.fetchDatabase(false, listener) == null)
        {
            throw new IOException("Database fetching failed");
        }

        // Refresh mirror list
        mirror.updateMirrors();
    }
}
