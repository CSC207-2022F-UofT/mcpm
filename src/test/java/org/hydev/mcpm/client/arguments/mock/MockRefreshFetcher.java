package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.models.Database;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    public void setDefaultResult(@Nullable Database database) {
        this.defaultResult = database;
    }
}
