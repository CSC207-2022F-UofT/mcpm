package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.DatabaseManager;
import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.arguments.parsers.CommandParser;
import org.hydev.mcpm.client.arguments.parsers.ExportPluginsParser;
import org.hydev.mcpm.client.arguments.parsers.InfoParser;
import org.hydev.mcpm.client.arguments.parsers.InstallParser;
import org.hydev.mcpm.client.arguments.parsers.ListParser;
import org.hydev.mcpm.client.arguments.parsers.LoadParser;
import org.hydev.mcpm.client.arguments.parsers.MirrorParser;
import org.hydev.mcpm.client.arguments.parsers.PageParser;
import org.hydev.mcpm.client.arguments.parsers.RefreshParser;
import org.hydev.mcpm.client.arguments.parsers.ReloadParser;
import org.hydev.mcpm.client.arguments.parsers.SearchParser;
import org.hydev.mcpm.client.arguments.parsers.UninstallParser;
import org.hydev.mcpm.client.arguments.parsers.UnloadParser;
import org.hydev.mcpm.client.arguments.parsers.UpdateParser;
import org.hydev.mcpm.client.commands.controllers.ExportPluginsController;
import org.hydev.mcpm.client.commands.controllers.InfoController;
import org.hydev.mcpm.client.commands.controllers.InstallController;
import org.hydev.mcpm.client.commands.controllers.ListController;
import org.hydev.mcpm.client.commands.controllers.LoadController;
import org.hydev.mcpm.client.commands.controllers.MirrorController;
import org.hydev.mcpm.client.commands.controllers.PageController;
import org.hydev.mcpm.client.commands.controllers.RefreshController;
import org.hydev.mcpm.client.commands.controllers.ReloadController;
import org.hydev.mcpm.client.commands.controllers.SearchPackagesController;
import org.hydev.mcpm.client.commands.controllers.UninstallController;
import org.hydev.mcpm.client.commands.controllers.UnloadController;
import org.hydev.mcpm.client.commands.controllers.UpdateController;
import org.hydev.mcpm.client.local.LocalPluginTracker;
import org.hydev.mcpm.client.display.presenters.InstallPresenter;
import org.hydev.mcpm.client.updater.CheckForUpdatesInteractor;
import org.hydev.mcpm.client.list.ListAllInteractor;
import org.hydev.mcpm.client.matcher.MatchPluginsInteractor;
import org.hydev.mcpm.client.export.ExportInteractor;
import org.hydev.mcpm.client.local.LocalDatabaseFetcher;
import org.hydev.mcpm.client.display.progress.ProgressBarFetcherListener;
import org.hydev.mcpm.client.local.MirrorSelector;
import org.hydev.mcpm.client.search.SearchInteractor;
import org.hydev.mcpm.client.injector.LocalJarFinder;
import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.installer.InstallInteractor;
import org.hydev.mcpm.client.installer.SpigotPluginDownloader;
import org.hydev.mcpm.client.uninstall.Uninstaller;
import org.hydev.mcpm.client.updater.UpdateInteractor;

import java.util.List;
import java.util.stream.Stream;

/**
 * Class that produces default implementations of the Controller and ArgsParser classes.
 * These classes are used to handle commands.
 * Checkout Command.java for a process on adding a new command!
 */
public class CommandsFactory {
    // No instantiation.
    private CommandsFactory() { }

    /**
     * Creates a list of general parsers for the ArgsParser class.
     *
     * @param isMinecraft If we're in the minecraft env
     * @return Returns a list of argument parsers that work in any environment (Server & CLI).
     */
    public static List<CommandParser> baseParsers(boolean isMinecraft) {
        var pager = new PageController(20);
        var mirror = new MirrorSelector();
        var fetcher = new LocalDatabaseFetcher(mirror.selectedMirrorSupplier());
        // TODO: Change all implementation to SuperLocalPluginTracker
        var superTracker = new LocalPluginTracker();
        var jarFinder = new LocalJarFinder();

        var loader = isMinecraft ? new PluginLoader(jarFinder) : null;
        var listener = new ProgressBarFetcherListener();
        var searcher = new SearchInteractor(fetcher, listener);
        var installer = new InstallInteractor(
            new SpigotPluginDownloader(new Downloader(), mirror.selectedMirrorSupplier()),
            new DatabaseManager(superTracker, searcher),
            loader
        );
        var matcher = new MatchPluginsInteractor(fetcher, listener);
        var updateChecker = new CheckForUpdatesInteractor(matcher);
        var updater = new UpdateInteractor(updateChecker, installer, superTracker);

        // Controllers
        var exportPluginsController = new ExportPluginsController(new ExportInteractor(superTracker));
        var listController = new ListController(new ListAllInteractor(superTracker), updateChecker);
        var searchController = new SearchPackagesController(searcher, pager);
        var mirrorController = new MirrorController(mirror);
        var infoController = new InfoController(superTracker);

        var installController = new InstallController(installer);
        var uninstallController = new UninstallController(new Uninstaller(superTracker, loader, jarFinder));
        var updateController = new UpdateController(updater);
        var refreshController = new RefreshController(fetcher, listener, mirror);

        // Presenters
        var installPresenter = new InstallPresenter();

        /*
         * Add general parsers to this list!
         * All versions of MCPM will have access to these parsers.
         * If you're not sure if your command is server-only, add it to this list!
         */
        return List.of(
            new ExportPluginsParser(exportPluginsController),
            new ListParser(listController),
            new SearchParser(searchController),
            new MirrorParser(mirrorController),
            new InfoParser(infoController),
            new InstallParser(installController, installPresenter),
            new RefreshParser(refreshController),
            new PageParser(pager),
            new UninstallParser(uninstallController),
            new UpdateParser(updateController)
        );
    }

    /**
     * Creates a list of server-only parsers for the ArgsParser class.
     *
     * @return Returns a list of argument parsers that require the Server (Minecraft Bukkit Plugin) environment.
     */
    public static List<CommandParser> serverParsers() {
        var jarFinder = new LocalJarFinder();
        var loader = new PluginLoader(jarFinder);

        var loadController = new LoadController(loader);
        var reloadController = new ReloadController(loader);
        var unloadController = new UnloadController(loader);

        /*
         * Add server-only parsers to this list!
         * Server only commands will not show up in the MCPM CLI.
         * They'll only be accessible when a user tries to execute a command from the MCPM Server Plugin (in-game).
         */
        var serverOnly = List.of(
            new LoadParser(loadController),
            new ReloadParser(reloadController),
            new UnloadParser(unloadController)
        );

        return Stream.concat(baseParsers(true).stream(), serverOnly.stream()).toList();
    }

    /**
     * Creates an ArgsParser object that has general parsers (works in any environment).
     *
     * @return An ArgsParser object. Invoke ArgsParser#parse to see more.
     */
    public static ArgsParser baseArgsParser() {
        return new ArgsParser(baseParsers(false));
    }

    /**
     * Creates an ArgsParser object that has all (CLI & Server) parsers.
     *
     * @return An ArgsParser object. Invoke ArgsParser#parse to see more.
     */
    public static ArgsParser serverArgsParser() {
        return new ArgsParser(serverParsers());
    }
}
