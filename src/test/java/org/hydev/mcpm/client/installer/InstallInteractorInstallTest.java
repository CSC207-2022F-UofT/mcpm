package org.hydev.mcpm.client.installer;
import org.hydev.mcpm.client.database.*;
import org.hydev.mcpm.client.DatabaseManager;
import org.hydev.mcpm.client.DatabaseManagerSingleton;
import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.database.PluginMockFactory;
import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.installer.presenter.InstallPresenter;
import org.hydev.mcpm.client.installer.presenter.InstallResultPresenter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class InstallInteractorInstallTest {
    private static Downloader downloader = new Downloader();

    private static SpigotPluginDownloader spigotPluginDownloader = new SpigotPluginDownloader(downloader);

    private static DatabaseManager databaseManager = DatabaseManagerSingleton.getDatabaseManager();

    private static PluginLoader loader = null;

    private static InstallInteractor installInteractor = new InstallInteractor(spigotPluginDownloader,
                                                                        databaseManager,
                                                                        loader);
    private Consumer<String> log = new Consumer<>() {
        @Override
        public void accept(String s) {
            System.out.println(s);
        }
    };
    InstallResultPresenter resultPresenter = new InstallPresenter(log);

    @BeforeAll
    public static void setupDirectory() {
        new File("plugins").mkdirs();

    }


    @Test
    public void testInputWithDependency() {
        // Create an empty plugins directory
        InstallInteractorInstallTest.setupDirectory();

        // Input without dependency
        InstallInput installInput = new InstallInput("JedCore",
                                                            SearchPackagesType.BY_NAME,
                                                            true,
                                                            true );
        installInteractor.installPlugin(installInput, resultPresenter);

        assert (databaseManager.checkPluginInstalledByName("JedCore") == true);
        assert (databaseManager.checkPluginInstalledByName("ProjectKorra") == true);
    }

    @Test
    public void testInstallPluginLAlreadyExistsLocally() {
        // Create an empty plugins directory
        InstallInteractorInstallTest.setupDirectory();

        InstallInput installInput = new InstallInput("CoreProtect",
                SearchPackagesType.BY_NAME,
                true,
                true );

        installInteractor.installPlugin(installInput, resultPresenter);
        assert (databaseManager.checkPluginInstalledByName("CoreProtect") == true);
        assert (installInteractor.installPlugin(installInput, resultPresenter) == false);
    }

    @Test
    public void testInstallPluginNotFound() {
        // Create an empty plugins directory
        InstallInteractorInstallTest.setupDirectory();

        InstallInput installInput = new InstallInput("(UwU)",
                SearchPackagesType.BY_NAME,
                true,
                true);

        installInteractor.installPlugin(installInput, resultPresenter);
        assert (databaseManager.checkPluginInstalledByName("(UwU)") == false);
        assert (installInteractor.installPlugin(installInput, resultPresenter) == false);

        InstallInput Input = new InstallInput("AnyaAnya",
                SearchPackagesType.BY_NAME,
                true,
                true);
        assert (databaseManager.checkPluginInstalledByName("AnyaAnya") == false);
        assert (installInteractor.installPlugin(Input, resultPresenter) == false);

    }
}
