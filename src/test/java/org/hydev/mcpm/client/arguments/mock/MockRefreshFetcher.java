package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.models.Database;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Provides a mock implementation of the DatabaseFetcher interface for testing RefreshController.
 */
public class MockRefreshFetcher implements DatabaseFetcher {
    private boolean fetched = false;

    private @Nullable Database defaultResult = new Database(List.of());

    @Override
    public @Nullable Database fetchDatabase(boolean cache, DatabaseFetcherListener listener) {
        if (!cache) {
            fetched = true;
        }

        return defaultResult;
    }

    public boolean getFetched() {
        return fetched;
    }

    /**
     * Sets the default database result that this interface will return when invoked.
     *
     * @param database The default return value for the load method.
     */
    public void setDefaultResult(@Nullable Database database) {
        this.defaultResult = database;
    }
}
