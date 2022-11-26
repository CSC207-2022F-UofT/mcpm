package org.hydev.mcpm.client.uninstallUseCase;

import org.hydev.mcpm.client.database.LocalPluginTracker;
import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.injector.PluginNotFoundException;

/**
 * Uninstalls a plugin
 */
public interface UninstallBoundary {
    /**
     * Uninstalls plugin based on its given name
     * @param name given name of plugin
     */

    public UninstallResult uninstallPlugin(UninstallInput input, LocalPluginTracker lpt, LocalDatabaseFetcher ldf, DatabaseManager dbManage,
                                           SearchPackagesResult spr) throws PluginNotFoundException;
}
