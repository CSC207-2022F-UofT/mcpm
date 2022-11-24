package org.hydev.mcpm.client;

import org.hydev.mcpm.client.database.PluginTracker;
import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.installer.InstallResult;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;

import java.util.List;

/**
 * Database API
 */
public class DatabaseManager {

    private final PluginTracker localPluginTracker;
    private final SearchPackagesBoundary searchInteractor;

    public DatabaseManager(PluginTracker localPluginTracker, SearchPackagesBoundary searchInteractor) {
        this.localPluginTracker = localPluginTracker;
        this.searchInteractor = searchInteractor;
    }

    /**
     *
     * Search if the plugin exists, throw exception if it does not exist and return the information of
     * the pluigin needs to be installed.
     *
     * @param input the plugin input pending for installation
     */
    public SearchPackagesResult getSearchResult(InstallInput input) {
        SearchPackagesInput searchPackagesInput = new SearchPackagesInput(input.type(), input.name(), false);
        SearchPackagesResult searchPackageResult = searchInteractor.search(searchPackagesInput);

        return searchPackageResult;
    }

    /**
     * check if plugin already installed locally
     *
     * @param pluginVersion The version of the installed plugin
     * */
    public boolean checkPluginInstalled(PluginVersion pluginVersion) {
        var name = pluginVersion.meta().name();

        List<PluginYml> pluginInstalled = localPluginTracker.listInstalled();
        for (PluginYml pluginYml : pluginInstalled) {
            if (pluginYml != null && pluginYml.name() != null && pluginYml.name().equals(name)) {
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
