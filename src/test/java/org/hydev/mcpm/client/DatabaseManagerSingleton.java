package org.hydev.mcpm.client;

import org.hydev.mcpm.client.database.LocalPluginTracker;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.searchusecase.SearchInteractor;
import org.hydev.mcpm.client.database.*;

import java.net.URI;

public class DatabaseManagerSingleton {
    static URI host = URI.create("https://mcpm.hydev.org");
    static DatabaseFetcher fetcher = new LocalDatabaseFetcher(host);
    static LocalPluginTracker tracker = new LocalPluginTracker();
    static SearchInteractor searcher = new SearchInteractor(fetcher);

    private static DatabaseManager databaseManager = new DatabaseManager(tracker, searcher);

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public static LocalPluginTracker getLocalPluginTracker() {
        return tracker;
    }
}
