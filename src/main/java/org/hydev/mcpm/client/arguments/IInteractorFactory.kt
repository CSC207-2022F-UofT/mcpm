package org.hydev.mcpm.client.arguments

import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener
import org.hydev.mcpm.client.database.mirrors.MirrorSelectBoundary
import org.hydev.mcpm.client.database.tracker.PluginTracker
import org.hydev.mcpm.client.export.ExportPluginsBoundary
import org.hydev.mcpm.client.export.ImportPluginsBoundary
import org.hydev.mcpm.client.installer.IInstaller
import org.hydev.mcpm.client.installer.PluginDownloader
import org.hydev.mcpm.client.list.ListAllBoundary
import org.hydev.mcpm.client.loader.*
import org.hydev.mcpm.client.matcher.MatchPluginsBoundary
import org.hydev.mcpm.client.search.SearchPackagesBoundary
import org.hydev.mcpm.client.uninstall.UninstallBoundary
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary
import org.hydev.mcpm.client.updater.UpdateBoundary

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2023-01-04
 */
interface IInteractorFactory
{
    val mirrorSelector: MirrorSelectBoundary
    val databaseFetcher: DatabaseFetcher
    val tracker: PluginTracker
    val jarFinder: LocalJarBoundary
    val pluginLoader: PluginLoader?
    val loader: LoadBoundary?
    val reloader: ReloadBoundary?
    val unloader: UnloadBoundary?
    val fetcherListener: DatabaseFetcherListener
    val searcher: SearchPackagesBoundary
    val pluginDownloader: PluginDownloader
    val installer: IInstaller
    val matcher: MatchPluginsBoundary
    val updateChecker: CheckForUpdatesBoundary
    val updater: UpdateBoundary
    val exporter: ExportPluginsBoundary
    val importer: ImportPluginsBoundary
    val lister: ListAllBoundary
    val uninstaller: UninstallBoundary
}
