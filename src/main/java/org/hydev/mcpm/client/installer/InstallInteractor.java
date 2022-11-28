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
import org.hydev.mcpm.client.installer.presenter.ResultPresenter;
import org.hydev.mcpm.client.models.PluginModel;

import java.io.File;
import java.net.URI;
import java.util.function.Consumer;

/**
 * Implementation to the InstallBoundary, handles installation of plugins

 * @author Azalea (https://github.com/hykilpikonna)
 * @author Rena (https://github.com/thudoan1706)
 * @author Taylor (https://github.com/1whatleytay)
 * @since 2022-11-20
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

    /*
     * Install the plugin
     * @param installInput: the plugin submitted by the user for installing
     * */
    @Override
    public boolean installPlugin(InstallInput installInput,
                                       ResultPresenter resultPresenter)
    {
        // 1. Search the name and get a list of plugins
        SearchPackagesResult searchResult = databaseManager.getSearchResult(installInput);

        if (searchResult.state() != SearchPackagesResult.State.SUCCESS) {
            InstallResult installResult = new InstallResult(
                    searchResult.state() == SearchPackagesResult.State.FAILED_TO_FETCH_DATABASE ?
                            InstallResult.Type.SEARCH_FAILED_TO_FETCH_DATABASE :
                            InstallResult.Type.SEARCH_INVALID_INPUT);
            resultPresenter.displayResult(installInput.name() + ": " + installResult.type().reason());
            return false;
        }

        if (searchResult.plugins().isEmpty()) {
            InstallResult installResult = new InstallResult(Type.NOT_FOUND);
            resultPresenter.displayResult(installInput.name() + ": " + installResult.type().reason());
            return false;
        }

        // 2. Pick the plugin id
        // TODO: Let the user pick a plugin ID (Future)
        PluginModel latestPluginModel = searchResult.plugins().get(0);
        long id = latestPluginModel.id();
        for (var plugin : searchResult.plugins()) {
            if (plugin.id() > id) {
                id = plugin.id();
                latestPluginModel = plugin;
            }
        }

        var pluginVersion = latestPluginModel.getLatestPluginVersion().orElse(null);
        if (pluginVersion == null) {
            InstallResult installResult = new InstallResult(Type.NO_VERSION_AVAILABLE);
            resultPresenter.displayResult(installInput.name() + ": " + installResult.type().reason());
            return false;
        }

        String pluginName = pluginVersion.meta().name();
        if (databaseManager.checkPluginInstalledByName(pluginName)) {
            InstallResult installResult = new InstallResult(Type.PLUGIN_EXISTS);
            resultPresenter.displayResult(installInput.name() + ": " + installResult.type().reason());
        } else {
            // 3. Download it
            spigotPluginDownloader.download(id, pluginVersion.id(),
                    "plugins/" + pluginVersion.meta().name() + ".jar");
            databaseManager.addManualInstalled(pluginVersion, installInput.isManuallyInstalled());

            // 4. Load the plugin
            if (pluginLoader != null && installInput.load()) {
                try {
                    pluginLoader.loadPlugin(installInput.name());
                } catch (PluginNotFoundException e) {
                    InstallResult installResult = new InstallResult(Type.SUCCESS_INSTALLED_AND_FAIL_LOADED);
                    resultPresenter.displayResult(installInput.name() + ": " + installResult.type().reason());
                    return false;
                }
            }

            if (installInput.load()) {
                InstallResult installResult = new InstallResult(Type.SUCCESS_INSTALLED_AND_LOADED);
                resultPresenter.displayResult(installInput.name() + ": " + installResult.type().reason());
            } else {
                InstallResult installResult = new InstallResult(Type.SUCCESS_INSTALLED_AND_UNLOADED);
                resultPresenter.displayResult(installInput.name() + ": " + installResult.type().reason());
            }
        }

        // 5 Installing the dependency of that plugin
        if (pluginVersion.meta().depend() != null) {
            for (String dependency : pluginVersion.meta().depend()) {
                InstallInput dependencyInput = new InstallInput(dependency,
                        SearchPackagesType.BY_NAME,
                        installInput.load(), false);
                installPlugin(dependencyInput, resultPresenter);
            }
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
        ResultPresenter resultPresenter = new InstallPresenter(log);
        var host = URI.create("https://mcpm.hydev.org");
        var fetcher = new LocalDatabaseFetcher(() -> host);
        var tracker = new LocalPluginTracker();
        var searcher = new SearchInteractor(fetcher);
        Downloader downloader = new Downloader();
        PluginLoader loader = null;
        SpigotPluginDownloader spigotPluginDownloader = new SpigotPluginDownloader(downloader, () -> host);
        DatabaseManager databaseManager = new DatabaseManager(tracker, searcher);
        InstallInteractor installInteractor = new InstallInteractor(spigotPluginDownloader, databaseManager, loader);
        InstallInput installInput = new InstallInput("JedCore",
                                                    SearchPackagesType.BY_NAME,
                                                    true,
                                                    true);
        installInteractor.installPlugin(installInput, resultPresenter);
    }
}
