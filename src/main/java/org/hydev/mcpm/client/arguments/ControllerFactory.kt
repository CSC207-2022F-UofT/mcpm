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
data class ControllerFactory(val boundary: InteractorFactoryBoundary) : ControllerFactoryBoundary
{
    override fun pageBoundary() = PageController(20)
    override fun exportController() = ExportController(boundary.exportBoundary(), LogExportPresenter())
    override fun importController() = ImportController(boundary.importBoundary(), LogImportPresenter())
    override fun listController() = ListController(boundary.listBoundary())
    override fun searchController() = SearchPackagesController(boundary.searchBoundary())
    override fun mirrorController() = MirrorController(boundary.mirrorSelector())
    override fun infoController() = InfoController(boundary.pluginTracker())
    override fun uninstallController() = UninstallController(boundary.uninstallBoundary())
    override fun updateController() = UpdateController(boundary.updateBoundary())

    override fun refreshController() = RefreshController(
        boundary.databaseFetcher(),
        boundary.fetcherListener(),
        boundary.mirrorSelector()
    )

    override fun loadController() = LoadController(boundary.loadBoundary())
    override fun reloadController() = ReloadController(boundary.reloadBoundary())
    override fun unloadController() = UnloadController(boundary.unloadBoundary())
}
