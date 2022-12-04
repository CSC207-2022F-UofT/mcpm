package org.hydev.mcpm.client.installer;

import org.bukkit.plugin.Plugin;
import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.local.LocalDatabaseFetcher;
import org.hydev.mcpm.client.local.LocalPluginTracker;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.hydev.mcpm.client.search.SearchPackagesResult;
import org.hydev.mcpm.client.search.SearchInteractor;
import org.hydev.mcpm.client.installer.output.InstallResult;
import org.hydev.mcpm.client.local.SuperLocalPluginTracker;
import org.hydev.mcpm.client.search.*;
import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.installer.output.InstallResult.Type;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.display.presenters.InstallPresenter;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.utils.ColorLogger;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation to the InstallBoundary, handles installation of plugins
 * */

public class InstallInteractor implements InstallBoundary {
    private static final String FILEPATH = "plugins";

    private final PluginDownloader spigotPluginDownloader;

    private final LoadBoundary pluginLoader;

    private final SearchPackagesBoundary searchInteractor;

    private final PluginTracker localPluginTracker;

    /**
     * Instantiate an interactor for install use case
     *
     * @param spigotPluginDownloader Plugin downloader for the MCPM Plugin Repository
     * @param pluginLoader loader for a locally installed plugin.
     */
    public InstallInteractor(PluginDownloader spigotPluginDownloader,
                             LoadBoundary pluginLoader,
                             SearchPackagesBoundary searchInteractor,
                             PluginTracker localPluginTracker) {
        this.spigotPluginDownloader = spigotPluginDownloader;
        this.pluginLoader = pluginLoader;
        this.searchInteractor = searchInteractor;
        this.localPluginTracker = localPluginTracker;
    }

    /**
     * Install the plugin.
     *
     * @param installInput the plugin submitted by the user for installing
     */
    @Override
    public List<InstallResult> installPlugin(InstallInput installInput)
    {
        var pluginName = installInput.name();

        // 1. Search the name and get a list of plugins
        SearchPackagesResult searchResult = getSearchResult(installInput);
        if (searchResult.state() == SearchPackagesResult.State.INVALID_INPUT) {
            return List.of(new InstallResult(Type.SEARCH_INVALID_INPUT, pluginName));
        }

        if (searchResult.state() == SearchPackagesResult.State.FAILED_TO_FETCH_DATABASE) {
            return List.of(new InstallResult(Type.SEARCH_FAILED_TO_FETCH_DATABASE, pluginName));
        }

        if (searchResult.plugins().isEmpty()) {
            return List.of(new InstallResult(Type.NOT_FOUND, pluginName));
        }

        // 2. Get the latest version of the plugin for the user
        // TODO: Let the user pick a plugin ID (Future)
        PluginModel pluginModel = getLastestPluginModel(searchResult);
        var idPluginModel = pluginModel.id();
        var pluginVersion = pluginModel.getLatestPluginVersion().orElse(null);
        if (pluginVersion == null) {
            return List.of(new InstallResult(Type.NO_VERSION_AVAILABLE, pluginName));
        }

        var results = new ArrayList<InstallResult>();
        boolean ifPluginDownloaded = localPluginTracker.findIfInLockByName(pluginName);
        if (ifPluginDownloaded) {
            results.add(new InstallResult(Type.PLUGIN_EXISTS, pluginName));
        } else {
            // 3. Download it
            spigotPluginDownloader.download(pluginName, idPluginModel, pluginVersion.id());
            // 4. Add the installed plugin to the json file
            String pluginVersionId = String.valueOf(pluginVersion.id());
            String pluginModelId = String.valueOf(idPluginModel);
            //        superPluginTracker.addEntry(pluginName,
            //                             installInput.isManuallyInstalled() ||
            //                                    superPluginTracker.listManuallyInstalled().contains(pluginName),
            //                                    pluginVersionId,
            //                                    pluginModelId);
            // 5. Add success installed
            results.add(new InstallResult(Type.SUCCESS_INSTALLED, pluginName));
        }

        // 5. Installing the dependency of that plugin
        if (pluginVersion.meta().depend() != null) {
            for (String dependency : pluginVersion.meta().depend()) {
                var dependencyInput = new InstallInput(dependency,
                        SearchPackagesType.BY_NAME,
                        installInput.load(), false);
                var rec = installPlugin(dependencyInput);
                results.addAll(rec);
            }
        }

        // 6. Load the plugin
        var loadResult = loadPlugin(pluginName, installInput.load());
        if (!loadResult) {
            results.add(new InstallResult(Type.UNLOADED, pluginName, loadResult));
        } else {
            results.add(new InstallResult(Type.LOADED, pluginName, loadResult));
        }
        return results;
    }

    /**
     *
     * Search if the plugin exists, return the Search Results
     *
     * @param input the input pending for installation
     */
    private SearchPackagesResult getSearchResult(InstallInput input) {
        SearchPackagesInput searchPackagesInput = new SearchPackagesInput(input.type(), input.name(), false);
        SearchPackagesResult searchPackageResult = searchInteractor.search(searchPackagesInput);
        return searchPackageResult;
    }

    /**
     * Pick the plugin id for the user
     *
     * @param searchPackagesResult Search Results of the plugin
     */
    private PluginModel getLastestPluginModel(SearchPackagesResult searchPackagesResult) {
        PluginModel latestPluginModel = searchPackagesResult.plugins().get(0);
        long id = latestPluginModel.id();
        for (var plugin : searchPackagesResult.plugins()) {
            if (plugin.id() > id) {
                latestPluginModel = plugin;
            }
        }
        return latestPluginModel;
    }

    /**
     * Load the plugin based on user's choice
     *
     * @param name Name of the plugin
     * @param load Whether to load the plugin
     * @return Whether the plugin is successfully loaded
     */
    private boolean loadPlugin(String name, boolean load) {
        if (!load || pluginLoader == null) return false;

        try {
            pluginLoader.loadPlugin(name);
            return true;
        } catch (PluginNotFoundException e) {
            return false;
        }
    }

    /**
     * A demo for installer.
     *
     * @param args Arguments are ignored.
     */
    public static void main(String[] args) {
        //noinspection ResultOfMethodCallIgnored
        new File(FILEPATH).mkdirs();
        var log = ColorLogger.toStdOut();
        var resultPresenter = new InstallPresenter();
        var host = URI.create("https://mcpm.hydev.org");
        var fetcher = new LocalDatabaseFetcher(() -> host);
        var superTracker = new SuperLocalPluginTracker();
        var searcher = new SearchInteractor(fetcher);
        Downloader downloader = new Downloader();
        PluginLoader loader = null;
        SpigotPluginDownloader spigotPluginDownloader = new SpigotPluginDownloader(downloader, () -> host);
        LocalPluginTracker tracker = new LocalPluginTracker();
        InstallInteractor installInteractor = new InstallInteractor(spigotPluginDownloader, loader,
                                                                    searcher, tracker);
        InstallInput installInput = new InstallInput("JedCore",
                                                    SearchPackagesType.BY_NAME,
                                                    true,
                                                    true);
        var result = installInteractor.installPlugin(installInput);
        resultPresenter.displayResult(result, log);
    }
}
