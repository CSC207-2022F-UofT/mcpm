package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.installer.output.InstallResult;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the Install Interactor
 */
public class InstallResultTypeTest_Success {

    /**
     * Test Setup: Create an empty directory before testing
     */
    @BeforeAll
    public static void dirSetUp() {
        File file = new File("plugins");
        boolean isDirectoryCreated = file.mkdir();
        System.out.println(isDirectoryCreated);
        if (!isDirectoryCreated) {
            deleteDir(file);  // Invoke recursive method
            file.mkdir();
        }
    }

    /**
     * Empty file in the directory
     */
    public static void deleteDir (File dir){
        File[] files = dir.listFiles();

        for (File pluginFile : files) {
            if (pluginFile.isDirectory()) {
                deleteDir(pluginFile);
            }
            pluginFile.delete();
        }
    }

    /**
     * Test case: Successfully installed plugin
     */
    @Test
    public void testPluginInstalledSucess() {

        InstallSetUp installSetUp = InstallSetUp.getInstaller();
        InstallInput installInput = new InstallInput("PandaPL",
                                                            SearchPackagesType.BY_NAME,
                                                            false,
                                                            true);
        InstallInteractor installInteractor = installSetUp.getInstallInteractor();
        List<InstallResult> listInstallResult = installInteractor.installPlugin(installInput);

        InstallResult JedCoreInstallResult = listInstallResult.get(0);
        assertEquals(JedCoreInstallResult.name(),"PandaPL");
        assertEquals(JedCoreInstallResult.type(), InstallResult.Type.SUCCESS_INSTALLED);
    }

    /**
     * Test case: User wants load the plugin
     */
    @Test
    public void testPluginInstalledUnLoad() {

        InstallSetUp installSetUp = InstallSetUp.getInstaller();
        InstallInput installInput = new InstallInput("PandaPL",
                                                            SearchPackagesType.BY_NAME,
                                                            false,
                                                            true);
        InstallInteractor installInteractor = installSetUp.getInstallInteractor();
        List<InstallResult> listInstallResult = installInteractor.installPlugin(installInput);

        InstallResult pandaPLLoadResult = listInstallResult.get(1);
        assertEquals(pandaPLLoadResult.name(),"PandaPL");
        assertEquals(pandaPLLoadResult.type(), InstallResult.Type.UNLOADED);
    }

    /**
     * Test case: User doesn't want to load the plugin
     * Noted: This test would be failed loaded because the loader can only by instantiated
     *        once the minecraft server is running
     */
    @Test
    public void testPluginInstalledLoad_BUT_UNLOADED() {
        InstallSetUp installSetUp = InstallSetUp.getInstaller();
        InstallInput installInput = new InstallInput("PandaPL",
                                                     SearchPackagesType.BY_NAME,
                                                    true,
                                                    true);
        InstallInteractor installInteractor = installSetUp.getInstallInteractor();
        List<InstallResult> listInstallResult = installInteractor.installPlugin(installInput);
        assertEquals(listInstallResult.size(), 2);

        InstallResult pandaPLLoadResult = listInstallResult.get(1);
        assertEquals(pandaPLLoadResult.name(),"PandaPL");
        assertEquals(pandaPLLoadResult.type(), InstallResult.Type.UNLOADED);
    }
}
