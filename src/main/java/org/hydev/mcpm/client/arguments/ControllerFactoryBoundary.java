package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.commands.controllers.*;

/**
 * Abstract factory for creating controller classes.
 * Classes can implement this in order to provide their own ways of initializing Controllers.
 * <p>
 * The methods of this class should be cached
 * (e.g. you should be able to invoke them many times and get the same object).
 */
public interface ControllerFactoryBoundary {
    /**
     * Creates a page boundary object that handles pagination and persisting of pages (for the `page` command).
     * <p>
     * PageBoundary/PageController technically acts as a controller in this case.
     * And is used for instantiation of other Controller classes.
     *
     * @return A PageBoundary object.
     */
    PageBoundary pageBoundary();

    /**
     * Creates an export controller that interfaces with ExportPluginsBoundary.
     *
     * @return A ExportController object.
     */
    ExportController exportController();

    /**
     * Creates an import controller that interfaces with ImportPluginsBoundary.
     *
     * @return A ImportController object.
     */
    ImportController importController();
    /**
     * Creates a list controller that interfaces with ListAllBoundary.
     *
     * @return A ListController object.
     */

    ListController listController();

    /**
     * Creates a search controller that interfaces with SearchPackagesBoundary.
     *
     * @return A SearchPackagesController object.
     */
    SearchPackagesController searchController();

    /**
     * Creates a mirror controller that interfaces with MirrorSelectorBoundary.
     *
     * @return A MirrorController object.
     */
    MirrorController mirrorController();

    /**
     * Creates an info controller that presents the user installed plugin info.
     *
     * @return A InfoController object.
     */
    InfoController infoController();

    /**
     * Creates a mirror controller that interfaces with InstallBoundary.
     *
     * @return A InstallController object.
     */
    InstallController installController();

    /**
     * Creates an `uninstall` controller that interfaces with UninstallBoundary.
     *
     * @return A UninstallController object.
     */
    UninstallController uninstallController();

    /**
     * Creates an update controller that interfaces with UpdateBoundary.
     *
     * @return A UpdateController object.
     */
    UpdateController updateController();

    /**
     * Creates a refresh controller that fetches the database in case it gets out of date.
     *
     * @return A RefreshController object.
     */
    RefreshController refreshController();

    /**
     * Creates a load controller that interfaces with LoadBoundary.
     *
     * @return A LoadController object.
     */
    LoadController loadController();

    /**
     * Creates a reload controller that interfaces with ReloadBoundary.
     *
     * @return A ReloadController object.
     */
    ReloadController reloadController();

    /**
     * Creates an unload controller that interfaces with UnloadBoundary.
     *
     * @return A UnloadController object.
     */
    UnloadController unloadController();
}
