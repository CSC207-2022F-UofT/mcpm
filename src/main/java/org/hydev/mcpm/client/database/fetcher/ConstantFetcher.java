package org.hydev.mcpm.client.database.fetcher;

import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.models.Database;
import org.hydev.mcpm.client.models.PluginModel;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Database fetcher that takes in a database
 */
public class ConstantFetcher implements DatabaseFetcher {
    private final Database database;

    public ConstantFetcher(Database database) {
        this.database = database;
    }

    public ConstantFetcher(List<PluginModel> plugins) {
        this(new Database(plugins));
    }

    @Override
    public @Nullable Database fetchDatabase(boolean cache, DatabaseFetcherListener listener) {
        return database;
    }
}
