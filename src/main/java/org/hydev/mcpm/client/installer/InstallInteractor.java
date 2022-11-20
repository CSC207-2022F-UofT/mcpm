package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.database.DatabaseInteractor;
import org.hydev.mcpm.client.database.LocalPluginTracker;
import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.database.results.ListPackagesResult;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.database.results.SearchPackagesResult.State;
import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.installer.InstallException.Type;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginYml;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation to the InstallBoundary, handles installation of plugins
 *
 * @author Rena (https://github.com/thudoan1706)
 * @since 2022-11-20
 */
public class InstallInteractor implements InstallBoundary {

    @Override
    public void installPlugin(InstallInput input) throws InstallException
    {
        // 1. Search the name and get a list of plugins
        var searchInput = new SearchPackagesInput(input.type(), input.name(), false);
        var searchResult = input.searchPackage().search(searchInput);
        if (searchResult.state() != State.SUCCESS) {
            throw new InstallException(
                searchResult.state() == State.FAILED_TO_FETCH_DATABASE ? Type.SEARCH_FAILED_TO_FETCH_DATABASE :
                Type.SEARCH_INVALID_INPUT);
        }

        if (searchResult.plugins().isEmpty()) {
            throw new InstallException(Type.NOT_FOUND);
        }

        // 2. Pick the plugin id
        // TODO: Let the user pick a plugin ID
        PluginModel latestPluginModel = searchResult.plugins().get(0);
        long id = latestPluginModel.id();
        for (var plugin : searchResult.plugins()) {
            if (plugin.id() > id) {
                id = plugin.id();
                latestPluginModel = plugin;
            }
        }

        var pluginVersion = latestPluginModel.getLatestPluginVersion()
            .orElseThrow(() -> new InstallException(Type.NO_VERSION_AVAILABLE));

        // 3. Download it
        var name = pluginVersion.meta().name();
        List<PluginYml> pluginInstalled = input.pluginTracker().listInstalled();
        for (PluginYml pluginYml : pluginInstalled) {
            if (pluginYml != null && pluginYml.name() != null && pluginYml.name().equals(name)) {
                throw new InstallException(Type.PLUGIN_EXISTS);
            }

        }

        // 4. Add it to the plugin tracker
        new File("plugins").mkdirs();
        input.pluginDownloader().download(id, pluginVersion.id(), "plugins/" + name + ".jar");
        input.pluginTracker().addManuallyInstalled(name);

        // 5. Install the dependencies
        if (pluginVersion.meta().depend() != null) {
            for (String dependency : pluginVersion.meta().depend()) {
                InstallInput dependencyInput = new InstallInput(dependency,
                        SearchPackagesType.BY_NAME,
                        input.filePath(),
                        input.load(),
                        input.searchPackage(),
                        input.pluginDownloader(),
                        input.pluginTracker());
                installPlugin(dependencyInput);
            }
        }
    }
}


