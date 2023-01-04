package org.hydev.mcpm.client.arguments

import org.hydev.mcpm.client.commands.controllers.*
import org.hydev.mcpm.client.display.presenters.LogExportPresenter
import org.hydev.mcpm.client.display.presenters.LogImportPresenter
import org.hydev.mcpm.client.local.PageController

/**
 * Handles the creation of default factory classes.
 * The required parameters to initialize these classes are acquired from the `boundary` object.
 *
 * @param boundary A provider to acquire the required interactors to initialize the controllers.
 */
@JvmRecord
data class ControllerFactory(val boundary: IInteractorFactory) : IControllerFactory
{
    override fun pageBoundary() = PageController(20)
    override fun exportController() = ExportController(boundary.exporter, LogExportPresenter())
    override fun importController() = ImportController(boundary.importer, LogImportPresenter())
    override fun listController() = ListController(boundary.lister)
    override fun searchController() = SearchPackagesController(boundary.searcher)
    override fun mirrorController() = MirrorController(boundary.mirrorSelector)
    override fun infoController() = InfoController(boundary.tracker)
    override fun uninstallController() = UninstallController(boundary.uninstaller)
    override fun updateController() = UpdateController(boundary.updater)

    override fun refreshController() = RefreshController(
        boundary.databaseFetcher,
        boundary.fetcherListener,
        boundary.mirrorSelector
    )

    override fun loadController() = LoadController(boundary.loader)
    override fun reloadController() = ReloadController(boundary.reloader)
    override fun unloadController() = UnloadController(boundary.unloader)
}
