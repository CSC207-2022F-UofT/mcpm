package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.database.mirrors.MirrorSelectBoundary;
import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.export.ExportPluginsBoundary;
import org.hydev.mcpm.client.export.ImportPluginsBoundary;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.PluginDownloader;
import org.hydev.mcpm.client.list.ListAllBoundary;
import org.hydev.mcpm.client.loader.LoadBoundary;
import org.hydev.mcpm.client.loader.LocalJarBoundary;
import org.hydev.mcpm.client.loader.ReloadBoundary;
import org.hydev.mcpm.client.loader.UnloadBoundary;
import org.hydev.mcpm.client.matcher.MatchPluginsBoundary;
import org.hydev.mcpm.client.search.SearchPackagesBoundary;
import org.hydev.mcpm.client.uninstall.UninstallBoundary;
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.updater.UpdateBoundary;

/**
 * Abstract factory for creating interactor classes.
 * Classes can implement this in order to provide their implementations of these boundary classes.
 * <p>
 * The methods of this class should be cached
 * (e.g. you should be able to invoke them many times and get the same object).
 * <p>
 * This class has the ability of creating a bunch of different boundaries.
 *
 */
public interface InteractorFactoryBoundary {
    /**
     * Creates a mirror boundary for managing currently selected and available mirrors.
     *
     * @return A MirrorSelectBoundary object.
     */
    MirrorSelectBoundary mirrorSelector();

    /**
     * Creates a database fetcher for grabbing a list of all available plugins.
     *
     * @return A DatabaseFetcher object.
     */
    DatabaseFetcher databaseFetcher();

    /**
     * Creates a plugin tracker object for keeping track of currently installed plugins.
     *
     * @return A PluginTracker object.
     */
    PluginTracker pluginTracker();

    /**
     * Creates a load boundary object for hot loading new plugins.
     *
     * @return A LoadBoundary object.
     */
    LoadBoundary loadBoundary();

    /**
     * Creates a load boundary object for hot reloading already running plugins.
     *
     * @return A ReloadBoundary object.
     */
    ReloadBoundary reloadBoundary();

    /**
     * Creates an unload boundary object for hot unloading running plugins.
     *
     * @return A UnloadBoundary object.
     */
    UnloadBoundary unloadBoundary();

    /**
     * Creates a jar boundary object for looking up plugins by name.
     *
     * @return A LocalJarBoundary object.
     */
    LocalJarBoundary jarBoundary();

    /**
     * Creates a fetcher listener object for sending database fetching updates to the user.
     *
     * @return A DatabaseFetcherListener object.
     */
    DatabaseFetcherListener fetcherListener();

    /**
     * Creates a search boundary object for looking up plugins in the database.
     *
     * @return A SearchPackagesBoundary object.
     */
    SearchPackagesBoundary searchBoundary();

    /**
     * Creates a plugin downloader object for downloading plugins to the filesystem.
     *
     * @return A PluginDownloader object.
     */
    PluginDownloader pluginDownloader();

    /**
     * Creates an `install` boundary object for installing new plugins.
     *
     * @return A InstallBoundary object.
     */
    InstallBoundary installBoundary();

    /**
     * Creates an `match` boundary object for finding plugins by id.
     *
     * @return A MatchPluginsBoundary object.
     */
    MatchPluginsBoundary matchBoundary();

    /**
     * Creates an `check for updates` boundary object for checking if plugins have available updates.
     *
     * @return A CheckForUpdatesBoundary object.
     */
    CheckForUpdatesBoundary checkForUpdatesBoundary();

    /**
     * Creates an `update` boundary object for updating installed plugins.
     *
     * @return A UpdateBoundary object.
     */
    UpdateBoundary updateBoundary();

    /**
     * Creates an `export` boundary object for exporting a list of installed plugins.
     *
     * @return A ExportPluginsBoundary object.
     */
    ExportPluginsBoundary exportBoundary();

    /**
     * Creates an `import` boundary object for exporting a list of installed plugins.
     *
     * @return A ImportPluginsBoundary object.
     */
    ImportPluginsBoundary importBoundary();
    /**
     * Creates an `list` boundary object for listing installed plugins.
     *
     * @return A ListAllBoundary object.
     */

    ListAllBoundary listBoundary();

    /**
     * Creates an `uninstall` boundary object for uninstalling plugins.
     *
     * @return A UninstallBoundary object.
     */
    UninstallBoundary uninstallBoundary();
}
