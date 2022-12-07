package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.commands.controllers.ExportPluginsController;
import org.hydev.mcpm.client.commands.controllers.InfoController;
import org.hydev.mcpm.client.commands.controllers.InstallController;
import org.hydev.mcpm.client.commands.controllers.ListController;
import org.hydev.mcpm.client.commands.controllers.LoadController;
import org.hydev.mcpm.client.commands.controllers.MirrorController;
import org.hydev.mcpm.client.commands.controllers.PageBoundary;
import org.hydev.mcpm.client.commands.controllers.RefreshController;
import org.hydev.mcpm.client.commands.controllers.ReloadController;
import org.hydev.mcpm.client.commands.controllers.SearchPackagesController;
import org.hydev.mcpm.client.commands.controllers.UninstallController;
import org.hydev.mcpm.client.commands.controllers.UnloadController;
import org.hydev.mcpm.client.commands.controllers.UpdateController;
import org.hydev.mcpm.client.local.PageController;

public record ControllerFactory(InteractorFactoryBoundary boundary) implements ControllerFactoryBoundary {
    @Override
    public PageBoundary pageBoundary() {
        return new PageController(20);
    }

    @Override
    public ExportPluginsController exportController() {
        return new ExportPluginsController(boundary.exportBoundary());
    }

    @Override
    public ListController listController() {
        return new ListController(boundary.listBoundary());
    }

    @Override
    public SearchPackagesController searchController() {
        return new SearchPackagesController(boundary.searchBoundary(), pageBoundary());
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
