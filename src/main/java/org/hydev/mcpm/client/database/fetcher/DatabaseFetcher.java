package org.hydev.mcpm.client.database.fetcher;

import org.hydev.mcpm.client.models.Database;
import org.jetbrains.annotations.Nullable;

/**
 * Exposes information for a class that can create a Database model for use in searches, etc.
 */
public interface DatabaseFetcher {
    /**
     * Invoked to construct a database object.
     *
     * @param cache If false, the underlying fetcher should always skip checking the cache if any exists.
     * @param listener Receives updates for database downloading events if the database is fetched from a stream.
     * @return The resulting database object.
     */
    @Nullable
    Database fetchDatabase(boolean cache, DatabaseFetcherListener listener);
}
