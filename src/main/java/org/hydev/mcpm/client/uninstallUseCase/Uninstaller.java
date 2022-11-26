package org.hydev.mcpm.client.uninstallUseCase;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import org.hydev.mcpm.client.commands.entries.UnloadController;
import org.hydev.mcpm.client.database.LocalPluginTracker;
import org.hydev.mcpm.client.database.PluginTracker;
import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.UnloadBoundary;
import org.hydev.mcpm.client.models.PluginModel;





public class Uninstaller implements UninstallBoundary {
    public UninstallResult main(UninstallInput input) throws PluginNotFoundException {
        LocalPluginTracker lpt = new LocalPluginTracker();
        LocalDatabaseFetcher ldf = new LocalDatabaseFetcher(URI.create("http://mcpm.hydev.org"));
        SearchInteractor searchInteract = new SearchInteractor(ldf);
        DatabaseManager dbManage = new DatabaseManager(lpt, searchInteract);
        InstallInput installInputCheckExistence = new InstallInput(input.name(),
                input.spType(), Arrays.asList(lpt.listManuallyInstalled()).contains(input.name()), false);
        // must create InstallInput object dince DatabaseManager.getSearchResult takes InstallInput only
        SearchPackagesResult spr = dbManage.getSearchResult(installInputCheckExistence);
        UninstallResult final_result = uninstallPlugin(input,lpt, ldf, dbManage,spr);

        if (!input.remove_depen()) {
            return final_result;
        }
        else {
            PluginModel pluginModel = spr.plugins().get(0);
            var pluginVers = pluginModel.getLatestPluginVersion().orElse(null);
            if (pluginModel.getLatestPluginVersion().equals(null) ) {
                return new UninstallResult(UninstallResult.State.LOCATING_PLUGIN_FAILURE);
            }
            if (pluginVers.meta().depend() != null) {
                var pluginsDependencies = pluginVers.meta().depend();
                if(removeDepend(pluginsDependencies)) { // at least one failure
                    return new UninstallResult(UninstallResult.State.LOCATING_PLUGIN_FAILURE);
                }


            }


        }
        return new UninstallResult(UninstallResult.State.SUCCESS);
    }


    public UninstallResult uninstallPlugin
            (UninstallInput input, LocalPluginTracker lpt, LocalDatabaseFetcher ldf, DatabaseManager dbManage,
             SearchPackagesResult spr)
            throws PluginNotFoundException {
        // 1. Check if plugin exists
        if (spr.state() != State.SUCCESS) {
            return new UninstallResult(UninstallResult.State.LOCATING_PLUGIN_FAILURE);
        } else if (spr.plugins().isEmpty()) {
            return new UninstallResult(UninstallResult.State.LOCATING_PLUGIN_FAILURE);
        }
        // 2. Uninstall the plugin
        // a. unload plugin
//        PluginLoader pil = new PluginLoader();
//        pil.unloadPlugin(name);
        input.ulb().unloadPlugin(input.name());
        // b. remove local file
        var plugins_folder = new File("plugins");
        var plugins = Arrays.stream((plugins_folder.listFiles()))
                .filter(plugin -> plugin.getName().endsWith(input.name() + ".jar"));

        if (plugins.toList().isEmpty()) {
            return new UninstallResult(UninstallResult.State.LOCATING_PLUGIN_FAILURE);
        } else {
            File file = new File(plugins.toArray()[0].toString());
            if (!file.delete()) {
                return new UninstallResult(UninstallResult.State.LOCATING_PLUGIN_FAILURE);
            } else {
                // c. remove from manually installed list
                lpt.removeManuallyInstalled(input.name());
            }


        }



        return new UninstallResult(UninstallResult.State.SUCCESS);
    }
    public boolean removeDepend (ArrayList<String> pluginsDependencies) throws PluginNotFoundException {
        LocalPluginTracker lpt = new LocalPluginTracker();
        LocalDatabaseFetcher ldf = new LocalDatabaseFetcher(URI.create("http://mcpm.hydev.org"));
        SearchInteractor searchInteract = new SearchInteractor(ldf);
        DatabaseManager dbManage = new DatabaseManager(lpt, searchInteract);
        var manuallyInstalled = lpt.listManuallyInstalled();
        var results = new ArrayList<UninstallResult>();
        for (String dependency:pluginsDependencies) {
            if (!Arrays.asList(manuallyInstalled).contains(dependency)) {
                PluginLoader ulb = new PluginLoader();
                UninstallInput dependInput = new UninstallInput(dependency,ulb,SearchPackagesType.BY_NAME,
                        false);
                InstallInput installDependInputCheckExistence = new InstallInput(dependInput.name(),
                        dependInput.spType(), Arrays.asList(lpt.listManuallyInstalled()).contains(dependInput.name()), false);

                SearchPackagesResult spr = dbManage.getSearchResult(installDependInputCheckExistence);
                var result = uninstallPlugin(dependInput, lpt, ldf, dbManage, spr);
                results.add(result);


            }
        }
        return Arrays.asList(results).contains(UninstallResult.State.LOCATING_PLUGIN_FAILURE);

    }








}


