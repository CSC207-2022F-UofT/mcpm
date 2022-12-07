package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.database.mirrors.MirrorSelectBoundary;
import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.export.ExportPluginsBoundary;
import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.LocalJarBoundary;
import org.hydev.mcpm.client.injector.ReloadBoundary;
import org.hydev.mcpm.client.injector.UnloadBoundary;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.PluginDownloader;
import org.hydev.mcpm.client.list.ListAllBoundary;
import org.hydev.mcpm.client.matcher.MatchPluginsBoundary;
import org.hydev.mcpm.client.search.SearchPackagesBoundary;
import org.hydev.mcpm.client.uninstall.UninstallBoundary;
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.updater.UpdateBoundary;

public interface InteractorFactoryBoundary {
    MirrorSelectBoundary mirrorSelector();
    DatabaseFetcher databaseFetcher();
    PluginTracker pluginTracker();
    LoadBoundary loadBoundary();
    ReloadBoundary reloadBoundary();
    UnloadBoundary unloadBoundary();
    LocalJarBoundary jarBoundary();
    DatabaseFetcherListener fetcherListener();
    SearchPackagesBoundary searchBoundary();
    PluginDownloader pluginDownloader();
    InstallBoundary installBoundary();
    MatchPluginsBoundary matchBoundary();
    CheckForUpdatesBoundary checkForUpdatesBoundary();
    UpdateBoundary updateBoundary();
    ExportPluginsBoundary exportBoundary();
    ListAllBoundary listBoundary();
    UninstallBoundary uninstallBoundary();
}
