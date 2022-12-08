package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.commands.controllers.*;
import org.hydev.mcpm.client.display.presenters.LogExportPresenter;
import org.hydev.mcpm.client.display.presenters.LogImportPresenter;
import org.hydev.mcpm.client.local.PageController;

/**
 * Handles the creation of default factory classes.
 * The required parameters to initialize these classes are acquired from the `boundary` object.
 *
 * @param boundary A provider to acquire the required interactors to initialize the controllers.
 */
public record ControllerFactory(InteractorFactoryBoundary boundary) implements ControllerFactoryBoundary {
    @Override
    public PageBoundary pageBoundary() {
        return new PageController(20);
    }

    @Override
    public ExportController exportController() {
        return new ExportController(boundary.exportBoundary(), new LogExportPresenter()); // temporary presenter bandage
    }

    @Override
    public ImportController importController() {
        return new ImportController(boundary.importBoundary(), new LogImportPresenter()); // temporary presenter bandage
    }

    @Override
    public ListController listController() {
        return new ListController(boundary.listBoundary());
    }

    @Override
    public SearchPackagesController searchController() {
        return new SearchPackagesController(boundary.searchBoundary());
    }

    @Override
    public MirrorController mirrorController() {
        return new MirrorController(boundary.mirrorSelector());
    }

    @Override
    public InfoController infoController() {
        return new InfoController(boundary.pluginTracker());
    }

    @Override
    public InstallController installController() {
        return new InstallController(boundary.installBoundary());
    }

    @Override
    public UninstallController uninstallController() {
        return new UninstallController(boundary.uninstallBoundary());
    }

    @Override
    public UpdateController updateController() {
        return new UpdateController(boundary.updateBoundary());
    }

    @Override
    public RefreshController refreshController() {
        return new RefreshController(
            boundary.databaseFetcher(),
            boundary.fetcherListener(),
            boundary.mirrorSelector()
        );
    }

    @Override
    public LoadController loadController() {
        return new LoadController(boundary.loadBoundary());
    }

    @Override
    public ReloadController reloadController() {
        return new ReloadController(boundary.reloadBoundary());
    }

    @Override
    public UnloadController unloadController() {
        return new UnloadController(boundary.unloadBoundary());
    }
}
