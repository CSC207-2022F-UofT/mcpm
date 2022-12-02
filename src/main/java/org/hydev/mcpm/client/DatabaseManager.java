package org.hydev.mcpm.client;

import org.hydev.mcpm.client.database.LocalPluginTracker;
import org.hydev.mcpm.client.database.PluginTracker;
import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.database.searchusecase.SearchInteractor;
import org.hydev.mcpm.client.installer.InstallResult;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;

import java.net.URI;
import java.util.List;

/**
 * Database Manager uses Database API to provide searchResult and check the installed plugin locally
 * (Goal: avoid the dependency and cluster of parameters to connect to database)
 */
public class DatabaseManager {
    private final PluginTracker localPluginTracker;
    private final SearchPackagesBoundary searchInteractor;

    public DatabaseManager(PluginTracker tracker, SearchPackagesBoundary searcher) {
        this.localPluginTracker = tracker;
        this.searchInteractor = searcher;
    }

    /**
     *
     * Search if the plugin exists, return the Search Results
     *
     * @param input the input pending for installation
     */
    public SearchPackagesResult getSearchResult(InstallInput input) {
        SearchPackagesInput searchPackagesInput = new SearchPackagesInput(input.type(), input.name(), false);
        SearchPackagesResult searchPackageResult = searchInteractor.search(searchPackagesInput);
        if (searchPackageResult.state() == SearchPackagesResult.State.SUCCESS) {
            return searchPackageResult;
        }
        return null;
    }

    /**
     * check if plugin with given version already installed locally
     *
     * @param pluginName The version of the installed plugin
     * */
    public boolean checkPluginInstalledByName(String pluginName) {
        var name = pluginName;

        List<PluginYml> pluginInstalled = localPluginTracker.listInstalled();
        for (PluginYml pluginYml : pluginInstalled) {
            if (pluginYml != null && pluginYml.name() != null && pluginYml.name().equals(name)) {
                return true;
            }
        }
        return false;
    }


    /**
     * check if plugin with given name already installed locally
     *
     * @param pluginVersion The version of the installed plugin
     * */
    public boolean checkPluginInstalledByVersion(PluginVersion pluginVersion) {
        var version = pluginVersion.meta().version();

        List<PluginYml> pluginInstalled = localPluginTracker.listInstalled();
        for (PluginYml pluginYml : pluginInstalled) {
            if (pluginYml != null && pluginYml.version() != null &&
                    pluginYml.version().equals(pluginVersion.meta().version())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add the installed plugin to the csv file
     *
     * @param pluginVersion The version of the installed plugin
     * */
    public void addManualInstalled(PluginVersion pluginVersion, boolean isManuallyInstalled) {
        String pluginName = pluginVersion.meta().name();
        localPluginTracker.addEntry(pluginName, isManuallyInstalled || localPluginTracker.listManuallyInstalled()
                .contains(pluginName));

    }
}
