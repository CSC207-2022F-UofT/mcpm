package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.DatabaseManager;
import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.database.LocalPluginTracker;
import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.database.searchusecase.SearchInteractor;
import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.installer.InstallResult.Type;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.installer.presenter.InstallPresenter;
import org.hydev.mcpm.client.installer.presenter.InstallResultPresenter;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;

import java.io.File;
import java.net.URI;
import java.util.function.Consumer;

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
    public boolean installPlugin(InstallInput installInput,
                                       InstallResultPresenter installPresenter)
    {
        // 1. Search the name and get a list of plugins
        SearchPackagesResult searchResult = databaseManager.getSearchResult(installInput);
        if (searchResult == null) {
            installPresenter.displayResult(new InstallResult(Type.SEARCH_INVALID_INPUT), installInput.name());
            return false;
        }

        if (searchResult.plugins().isEmpty()) {
            installPresenter.displayResult(new InstallResult(Type.NOT_FOUND), installInput.name());
            return false;
        }

        // 2. Get the latest version of the plugin for the user
        // TODO: Let the user pick a plugin ID (Future)
        PluginModel pluginModel = getLastestPluginModel(searchResult);
        var idPluginModel = pluginModel.id();
        var pluginVersion = pluginModel.getLatestPluginVersion().orElse(null);
        if (pluginVersion == null) {
            installPresenter.displayResult(new InstallResult(Type.NO_VERSION_AVAILABLE), installInput.name());
            return false;
        }

        // Name of the latest version plugin
        String pluginName = pluginVersion.meta().name();

        if (databaseManager.checkPluginInstalledByName(pluginName)) {
            InstallResult installResult = new InstallResult(Type.PLUGIN_EXISTS);
            installPresenter.displayResult(installResult, installInput.name());
        } else {
            // 3. Download it
            spigotPluginDownloader.download(idPluginModel, pluginVersion.id(),
                    "plugins/" + pluginVersion.meta().name() + ".jar");
            databaseManager.addManualInstalled(pluginVersion, installInput.isManuallyInstalled());

            // 4. Load the plugin
            loadPlugin(installInput, installPresenter);
        }

        // 5 Installing the dependency of that plugin
        if (pluginVersion.meta().depend() != null) {
            for (String dependency : pluginVersion.meta().depend()) {
                InstallInput dependencyInput = new InstallInput(dependency,
                        SearchPackagesType.BY_NAME,
                        installInput.load(), false);
                installPlugin(dependencyInput, installPresenter);
            }
        }
        return true;
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
     * @param installInput Install Plugin Input
     * @param installPresenter display the state of plugin pending for installation
     */
    private boolean loadPlugin(InstallInput installInput, InstallResultPresenter installPresenter) {
        if (pluginLoader != null && installInput.load()) {
            try {
                pluginLoader.loadPlugin(installInput.name());
            } catch (PluginNotFoundException e) {
                installPresenter.displayResult(new InstallResult(Type.SUCCESS_INSTALLED_AND_FAIL_LOADED),
                                               installInput.name());
                return false;
            }
        }

        if (installInput.load()) {
            installPresenter.displayResult(new InstallResult(Type.SUCCESS_INSTALLED_AND_LOADED),
                                           installInput.name());
        } else {
            installPresenter.displayResult(new InstallResult(Type.SUCCESS_INSTALLED_AND_UNLOADED),
                                           installInput.name());
        }
        return true;
    }

    /**
     * A demo for installer.
     *
     * @param args Arguments are ignored.
     */
    public static void main(String[] args) {
        new File(FILEPATH).mkdirs();
        Consumer<String> log = new Consumer<>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        InstallResultPresenter resultPresenter = new InstallPresenter(log);
        var host = URI.create("https://mcpm.hydev.org");
        var fetcher = new LocalDatabaseFetcher(() -> host);
        var tracker = new LocalPluginTracker();
        var searcher = new SearchInteractor(fetcher);
        Downloader downloader = new Downloader();
        PluginLoader loader = null;
        SpigotPluginDownloader spigotPluginDownloader = new SpigotPluginDownloader(downloader, () -> host);
        DatabaseManager databaseManager = new DatabaseManager(tracker, searcher);
        InstallInteractor installInteractor = new InstallInteractor(spigotPluginDownloader, databaseManager, loader);

        InstallInput installInput = new InstallInput(
            "JedCore",
            SearchPackagesType.BY_NAME,
            true,
            true
        );

        installInteractor.installPlugin(installInput, resultPresenter);
    }
}
