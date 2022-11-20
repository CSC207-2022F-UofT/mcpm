package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.database.DatabaseInteractor;
import org.hydev.mcpm.client.database.LocalPluginTracker;
import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.database.results.ListPackagesResult;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginYml;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class InstallInteractor implements InstallBoundary {
    @Override
    public void installPlugin(InstallInput installInput) {
        SearchPackagesInput searchPackagesInput = new SearchPackagesInput(installInput.type(), installInput.name(), false);
        SearchPackagesResult searchPackageResult = installInput.searchPackage().search(searchPackagesInput);
        if (searchPackageResult.state() != SearchPackagesResult.State.SUCCESS) {
            throw new IllegalArgumentException("Illegal");
        }

        if (searchPackageResult.plugins().isEmpty()) {
            throw new RuntimeException();
        }

        PluginModel latestPluginModel = searchPackageResult.plugins().get(0);
        long id = latestPluginModel.id();
        for (PluginModel plugin: searchPackageResult.plugins()) {
            if (plugin.id() > id) {
                id = plugin.id();
                latestPluginModel = plugin;
            }
        }

        var pluginVersion1 = latestPluginModel.getLatestPluginVersion();
        if (pluginVersion1.isEmpty()) {
            throw new RuntimeException();
        }

        var pluginVersion = pluginVersion1.get();

        /*
    * 1. Search the name and get a list of plugins
    * 2. Pick the plugin id
    * 3. Construct the url
    * 4. Download it (Downloader)
    * 5. Add plugin tracker to check install and write
    * 6. Install the list of dependency
    *
    * */

        List<PluginYml> pluginInstalled = installInput.pluginTracker().listInstalled();
        for (PluginYml pluginYml:pluginInstalled) {
            if (pluginYml != null && pluginYml.name() != null && pluginYml.name().equals(pluginVersion.meta().name())) {
                throw new RuntimeException();
            }

        }
        new File("plugins").mkdirs();
        installInput.pluginDownloader().download(id, pluginVersion.id(), "plugins/" + pluginVersion.meta().name() + ".jar");
        installInput.pluginTracker().addManuallyInstalled(pluginVersion.meta().name());

        if (pluginVersion.meta().depend()!= null) {
            for (String dependency:pluginVersion.meta().depend()) {
                InstallInput dependencyInput = new InstallInput(dependency,
                        SearchPackagesType.BY_NAME,
                        installInput.filePath(),
                        installInput.load(),
                        installInput.searchPackage(),
                        installInput.pluginDownloader(),
                        installInput.pluginTracker());
                installPlugin(dependencyInput);
            }
        }
    }

    public static void main(String[] args) {
        Downloader downloader = new Downloader();
        InstallInteractor installInteractor = new InstallInteractor();
        SpigotPluginDownloader spigotPluginDownloader = new SpigotPluginDownloader(downloader);
        DatabaseInteractor databaseInteractor = new DatabaseInteractor(new LocalDatabaseFetcher(URI.create("http://mcpm.hydev.org")));
        LocalPluginTracker localPluginTracker = new LocalPluginTracker("lock", "plugins");

        InstallInput installInput = new InstallInput("JedCore", SearchPackagesType.BY_NAME, "",
                true, databaseInteractor, spigotPluginDownloader, localPluginTracker);
        installInteractor.installPlugin(installInput);
    }
}


