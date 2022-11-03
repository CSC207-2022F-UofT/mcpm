package org.hydev.mcpm.client.database.fetcher;

import org.hydev.mcpm.client.models.Database;
import org.jetbrains.annotations.Nullable;

public interface DatabaseFetcher {
    @Nullable
    Database fetchDatabase(boolean cache, DatabaseFetcherListener listener);
}
