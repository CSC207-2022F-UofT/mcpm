package org.hydev.mcpm.client;

import org.hydev.mcpm.client.database.DatabaseInteractor;
import org.hydev.mcpm.client.database.LocalPluginTracker;
import org.hydev.mcpm.client.database.PluginTracker;
import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.database.searchusecase.SearchInteractor;
import org.hydev.mcpm.client.installer.InstallException;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;

import java.net.URI;
import java.util.List;

/*
 * A manager that communicates with database and returns the plugin source for installation
 * */
public class DatabaseManager {

    private final PluginTracker localPluginTracker;
    private final SearchPackagesBoundary searchInteractor;

//    private LocalDatabaseFetcher localDatabaseFetcher = new LocalDatabaseFetcher(URI.create("http://mcpm.hydev.org"));

    /**
     * Initialize an database manager
     */
    public DatabaseManager(PluginTracker localPluginTracker, SearchPackagesBoundary searchInteractor) {
        this.localPluginTracker = localPluginTracker;
        this.searchInteractor = searchInteractor;
    }

//    /**
//     * Initialize an database manager
//     * @param listener The listener that will receive updates if the database is downloaded from the internet.
//     */
//    public DatabaseManager(DatabaseFetcherListener listener) {
//        this.databaseInteractor = new DatabaseInteractor(localDatabaseFetcher);
//        this.localPluginTracker = new LocalPluginTracker();
//        this.searchInteractor = new SearchInteractor(localDatabaseFetcher, listener);
//    }


    /**
     * Search if the plugin exists, throw exception if it does not exist and return the information of
     * the pluigin needs to be installed.
     * @param input: the plugin input pending for installation
     */
    public SearchPackagesResult getSearchResult(InstallInput input) throws InstallException {
        SearchPackagesInput searchPackagesInput = new SearchPackagesInput(input.type(), input.name(), false);
        SearchPackagesResult searchPackageResult = searchInteractor.search(searchPackagesInput);
        if (searchPackageResult.state() != SearchPackagesResult.State.SUCCESS) {
            throw new InstallException(
                    searchPackageResult.state() == SearchPackagesResult.State.FAILED_TO_FETCH_DATABASE ? InstallException.Type.SEARCH_FAILED_TO_FETCH_DATABASE :
                            InstallException.Type.SEARCH_INVALID_INPUT);
        }
        return searchPackageResult;
    }

    /**
     * check if plugin already installed locally
     * @param pluginVersion: The version of the installed plugin
     * */
    public void checkPluginInstalled(PluginVersion pluginVersion) throws InstallException {
        var name = pluginVersion.meta().name();

        List<PluginYml> pluginInstalled = localPluginTracker.listInstalled();
        for (PluginYml pluginYml : pluginInstalled) {
            if (pluginYml != null && pluginYml.name() != null && pluginYml.name().equals(name)) {
                throw new InstallException(InstallException.Type.PLUGIN_EXISTS);
            }
        }
    }

    /**
     * Add the installed plugin to the csv file
     * @param pluginVersion: The version of the installed plugin
     * */
    public void addManualInstalled(PluginVersion pluginVersion) {
        String pluginName = pluginVersion.meta().name();
        if (pluginVersion != null) {
            localPluginTracker.addEntry(pluginName, true);
        }
    }
}
