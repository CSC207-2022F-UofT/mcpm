package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;

/**
 * Controller for the refresh command
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-24
 */
public record RefreshController(DatabaseFetcher fetcher, DatabaseFetcherListener listener)
{
    /**
     * Refresh
     */
    public void refresh()
    {
        fetcher.fetchDatabase(false, listener);
    }
}
