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

public interface ControllerFactoryBoundary {
    // PageBoundary/PageController technically acts as a controller in this case.
    // And is used for instantiation of other Controller classes.
    PageBoundary pageBoundary();
    ExportPluginsController exportController();
    ListController listController();
    SearchPackagesController searchController();
    MirrorController mirrorController();
    InfoController infoController();
    InstallController installController();
    UninstallController uninstallController();
    UpdateController updateController();
    RefreshController refreshController();
    LoadController loadController();
    ReloadController reloadController();
    UnloadController unloadController();
}
