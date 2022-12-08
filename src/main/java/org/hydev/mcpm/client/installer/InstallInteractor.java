package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.display.progress.ProgressBarFetcherListener;
import org.hydev.mcpm.client.installer.input.ExactInstallInput;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.installer.output.InstallResult;
import org.hydev.mcpm.client.loader.LoadBoundary;
import org.hydev.mcpm.client.loader.PluginLoader;
import org.hydev.mcpm.client.loader.PluginNotFoundException;
import org.hydev.mcpm.client.local.LocalDatabaseFetcher;
import org.hydev.mcpm.client.local.LocalPluginTracker;
import org.hydev.mcpm.client.search.SearchPackagesBoundary;
import org.hydev.mcpm.client.search.SearchPackagesInput;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.hydev.mcpm.client.search.SearchPackagesResult;
import org.hydev.mcpm.client.search.SearchInteractor;
import org.hydev.mcpm.client.installer.input.FuzzyInstallInput;
import org.hydev.mcpm.client.display.presenters.InstallPresenter;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.utils.ColorLogger;
import org.hydev.mcpm.client.installer.output.InstallResult.Type;

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
    public List<InstallResult> installPlugin(InstallInput input) {
        PluginModel pluginModel;

        // 1. If the installer input is fuzzy, search and obtain a plugin of the name
        if (input instanceof FuzzyInstallInput installInput) {
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

            pluginModel = getLastestPluginModel(searchResult);
        }
        else if (input instanceof ExactInstallInput exact) {
            pluginModel = exact.model();
        }
        else {
            throw new UnsupportedOperationException("InstallInput type " + input.getClass() + " not supported.");
        }

        // 2. Get the latest version of the plugin for the user
        // TODO: Let the user pick a plugin ID (Future)
        var idPluginModel = pluginModel.id();
        var pluginVersion = pluginModel.getLatestPluginVersion().orElse(null);
        if (pluginVersion == null) {
            return List.of(new InstallResult(Type.NO_VERSION_AVAILABLE, idPluginModel + ""));
        }
        var pluginName = pluginVersion.meta().name();

        var results = new ArrayList<InstallResult>();
        // 3. Installing the dependency of that plugin
        if (pluginVersion.meta().depend() != null) {
            for (String dependency : pluginVersion.meta().depend()) {
                var dependencyInput = new FuzzyInstallInput(dependency,
                        SearchPackagesType.BY_NAME,
                        input.load(), false);
                var rec = installPlugin(dependencyInput);
                results.addAll(rec);
            }
        }
        boolean ifPluginDownloaded = localPluginTracker.findIfInLockByName(pluginName);
        if (!ifPluginDownloaded) {
            // 4. Download the plugin
            spigotPluginDownloader.download(pluginName, idPluginModel, pluginVersion.id());
            // 5. Add the installed plugin to the json file
            long pluginVersionId = pluginVersion.id();
            long pluginModelId = idPluginModel;
            localPluginTracker.addEntry(pluginName,
                    input.isManuallyInstalled(),
                    pluginVersionId,
                    pluginModelId);
        }

        // 6. Load the plugin
        var loadResult = loadPlugin(pluginName, input.load());

        // 7. Add success installed
        if (ifPluginDownloaded) {
            results.add(new InstallResult(Type.PLUGIN_EXISTS, pluginName));
        } else {
            results.add(new InstallResult(Type.SUCCESS_INSTALLED, pluginName));
        }
        return results;
    }

    /**
     *
     * Search if the plugin exists, return the Search Results
     *
     * @param input the input pending for installation
     */
    private SearchPackagesResult getSearchResult(FuzzyInstallInput input) {
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
        var listener = new ProgressBarFetcherListener();
        var searcher = new SearchInteractor(fetcher, listener);
        Downloader downloader = new Downloader();
        PluginLoader loader = null;
        SpigotPluginDownloader spigotPluginDownloader = new SpigotPluginDownloader(downloader, () -> host);
        LocalPluginTracker tracker = new LocalPluginTracker();
        InstallInteractor installInteractor = new InstallInteractor(spigotPluginDownloader, loader,
                                                                    searcher, tracker);
        FuzzyInstallInput installInput = new FuzzyInstallInput("JedCore",
                                                    SearchPackagesType.BY_NAME,
                                                    true,
                                                    true);
        var result = installInteractor.installPlugin(installInput);
        resultPresenter.displayResult(result, log);
    }
}
