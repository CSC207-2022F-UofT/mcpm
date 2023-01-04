package org.hydev.mcpm.client.arguments

import org.hydev.mcpm.client.Downloader
import org.hydev.mcpm.client.display.progress.ProgressBarFetcherListener
import org.hydev.mcpm.client.export.ExportInteractor
import org.hydev.mcpm.client.export.ImportInteractor
import org.hydev.mcpm.client.installer.Installer
import org.hydev.mcpm.client.installer.SpigotPluginDownloader
import org.hydev.mcpm.client.list.ListAllInteractor
import org.hydev.mcpm.client.loader.LocalJarFinder
import org.hydev.mcpm.client.loader.PluginLoader
import org.hydev.mcpm.client.local.LocalDatabaseFetcher
import org.hydev.mcpm.client.local.LocalPluginTracker
import org.hydev.mcpm.client.local.MirrorSelector
import org.hydev.mcpm.client.matcher.MatchPluginsInteractor
import org.hydev.mcpm.client.search.SearchInteractor
import org.hydev.mcpm.client.uninstall.PluginRemover
import org.hydev.mcpm.client.uninstall.Uninstaller
import org.hydev.mcpm.client.updater.CheckForUpdatesInteractor
import org.hydev.mcpm.client.updater.UpdateInteractor

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2023-01-04
 */
class InteractorFactory : IInteractorFactory
{
    override val mirrorSelector by lazy { MirrorSelector() }
    override val databaseFetcher by lazy { LocalDatabaseFetcher(mirrorSelector.selectedMirrorSupplier()) }
    override val tracker by lazy { LocalPluginTracker() }
    override val jarFinder by lazy { LocalJarFinder() }
    override val pluginLoader by lazy { PluginLoader(jarFinder) }
    override val loader by lazy { pluginLoader }
    override val reloader by lazy { pluginLoader }
    override val unloader by lazy { pluginLoader }
    override val fetcherListener by lazy { ProgressBarFetcherListener() }
    override val searcher by lazy { SearchInteractor(databaseFetcher, fetcherListener) }
    override val pluginDownloader by lazy { SpigotPluginDownloader(Downloader(), mirrorSelector.selectedMirrorSupplier()) }
    override val installer by lazy { Installer(pluginDownloader, loader, searcher, tracker) }
    override val matcher by lazy { MatchPluginsInteractor(databaseFetcher, fetcherListener) }
    override val updateChecker by lazy { CheckForUpdatesInteractor(matcher) }
    override val updater by lazy { UpdateInteractor(updateChecker, installer, tracker) }
    override val exporter by lazy { ExportInteractor(tracker) }
    override val importer by lazy { ImportInteractor(installer) }
    override val lister by lazy { ListAllInteractor(tracker, updateChecker) }
    override val uninstaller by lazy { Uninstaller(tracker, unloader, PluginRemover(jarFinder)) }
}
