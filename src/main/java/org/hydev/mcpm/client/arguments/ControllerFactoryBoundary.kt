package org.hydev.mcpm.client.arguments

import org.hydev.mcpm.client.commands.controllers.*

/**
 * Abstract factory for creating controller classes.
 * Classes can implement this in order to provide their own ways of initializing Controllers.
 *
 *
 * The methods of this class should be cached
 * (e.g. you should be able to invoke them many times and get the same object).
 */
interface ControllerFactoryBoundary
{
    /**
     * Creates a page boundary object that handles pagination and persisting of pages (for the `page` command).
     *
     *
     * PageBoundary/PageController technically acts as a controller in this case.
     * And is used for instantiation of other Controller classes.
     *
     * @return A PageBoundary object.
     */
    fun pageBoundary(): PageBoundary

    /**
     * Creates an export controller that interfaces with ExportPluginsBoundary.
     *
     * @return A ExportController object.
     */
    fun exportController(): ExportController

    /**
     * Creates an import controller that interfaces with ImportPluginsBoundary.
     *
     * @return A ImportController object.
     */
    fun importController(): ImportController

    /**
     * Creates a list controller that interfaces with ListAllBoundary.
     *
     * @return A ListController object.
     */
    fun listController(): ListController

    /**
     * Creates a search controller that interfaces with SearchPackagesBoundary.
     *
     * @return A SearchPackagesController object.
     */
    fun searchController(): SearchPackagesController

    /**
     * Creates a mirror controller that interfaces with MirrorSelectorBoundary.
     *
     * @return A MirrorController object.
     */
    fun mirrorController(): MirrorController

    /**
     * Creates an info controller that presents the user installed plugin info.
     *
     * @return A InfoController object.
     */
    fun infoController(): InfoController

    /**
     * Creates an `uninstall` controller that interfaces with UninstallBoundary.
     *
     * @return A UninstallController object.
     */
    fun uninstallController(): UninstallController

    /**
     * Creates an update controller that interfaces with UpdateBoundary.
     *
     * @return A UpdateController object.
     */
    fun updateController(): UpdateController

    /**
     * Creates a refresh controller that fetches the database in case it gets out of date.
     *
     * @return A RefreshController object.
     */
    fun refreshController(): RefreshController

    /**
     * Creates a load controller that interfaces with LoadBoundary.
     *
     * @return A LoadController object.
     */
    fun loadController(): LoadController

    /**
     * Creates a reload controller that interfaces with ReloadBoundary.
     *
     * @return A ReloadController object.
     */
    fun reloadController(): ReloadController

    /**
     * Creates an unload controller that interfaces with UnloadBoundary.
     *
     * @return A UnloadController object.
     */
    fun unloadController(): UnloadController
}
