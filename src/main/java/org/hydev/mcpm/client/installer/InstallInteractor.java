package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.DatabaseManager;
import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.local.LocalDatabaseFetcher;
import org.hydev.mcpm.client.local.LocalPluginTracker;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.hydev.mcpm.client.search.SearchPackagesResult;
import org.hydev.mcpm.client.search.SearchInteractor;
import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.installer.InstallResult.Type;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.display.presenters.InstallPresenter;
import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter;
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

    private final DatabaseManager databaseManager;
    private final PluginDownloader spigotPluginDownloader;

    private final LoadBoundary pluginLoader;

    /**
     * Instantiate an interactor for install use case
     *
     * @param spigotPluginDownloader Plugin downloader for the MCPM Plugin Repository
     * @param databaseManager Database API
     * @param pluginLoader loader for a locally installed plugin.
     */
    public InstallInteractor(PluginDownloader spigotPluginDownloader,
                             DatabaseManager databaseManager,
                             LoadBoundary pluginLoader) {
        this.databaseManager = databaseManager;
        this.spigotPluginDownloader = spigotPluginDownloader;
        this.pluginLoader = pluginLoader;
    }

    /**
     * Install the plugin.
     *
     * @param installInput the plugin submitted by the user for installing
     */
    @Override
    public List<InstallResult> installPlugin(InstallInput installInput)
    {
        var name = installInput.name();

        // 1. Search the name and get a list of plugins
        SearchPackagesResult searchResult = databaseManager.getSearchResult(installInput);
        if (searchResult == null) {
            return List.of(new InstallResult(Type.SEARCH_INVALID_INPUT, name));
        }

        if (searchResult.plugins().isEmpty()) {
            return List.of(new InstallResult(Type.NOT_FOUND, name));
        }

        // 2. Get the latest version of the plugin for the user
        // TODO: Let the user pick a plugin ID (Future)
        PluginModel pluginModel = getLastestPluginModel(searchResult);
        var idPluginModel = pluginModel.id();
        var pluginVersion = pluginModel.getLatestPluginVersion().orElse(null);
        if (pluginVersion == null) {
            return List.of(new InstallResult(Type.NO_VERSION_AVAILABLE, name));
        }

        // Name of the latest version plugin
        String pluginName = pluginVersion.meta().name();

        if (databaseManager.checkPluginInstalledByName(pluginName)) {
            return List.of(new InstallResult(Type.PLUGIN_EXISTS, name));
        }

        // 3. Download it
        spigotPluginDownloader.download(idPluginModel, pluginVersion.id(),
                "plugins/" + pluginVersion.meta().name() + ".jar");

        // 4. Installing the dependency of that plugin
        var results = new ArrayList<InstallResult>();
        if (pluginVersion.meta().depend() != null) {
            for (String dependency : pluginVersion.meta().depend()) {
                var dependencyInput = new InstallInput(dependency,
                        SearchPackagesType.BY_NAME,
                        installInput.load(), false);
                var rec = installPlugin(dependencyInput);
                results.addAll(rec);
            }
        }

        // 5. Load the plugin
        var loadResult = loadPlugin(name, installInput.load());

        // 6. Add success
        results.add(0, new InstallResult(Type.SUCCESS_INSTALLED, name, loadResult));

        return results;
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
        var searcher = new SearchInteractor(fetcher);
        Downloader downloader = new Downloader();
        PluginLoader loader = null;
        SpigotPluginDownloader spigotPluginDownloader = new SpigotPluginDownloader(downloader, () -> host);
        LocalPluginTracker tracker = new LocalPluginTracker();
        DatabaseManager databaseManager = new DatabaseManager(tracker, searcher);
        InstallInteractor installInteractor = new InstallInteractor(spigotPluginDownloader, databaseManager, loader);
        InstallInput installInput = new InstallInput("JedCore",
                                                    SearchPackagesType.BY_NAME,
                                                    true,
                                                    true);
        var result = installInteractor.installPlugin(installInput);
        resultPresenter.displayResult(result, log);
    }
}
