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
 * Tests the Install Interactor: successfully installing the dependency of the plugin
 */
public class InstallDependencyTest {

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
     * Test case: Successfully install the plugin with the dependency
     */
    @Test
    public void test_InstallDependency_OfInputPlugin() {
        InstallSetUp installSetUp = InstallSetUp.getInstaller();
        InstallInput installInput = new InstallInput("JedCore",
                                                            SearchPackagesType.BY_NAME,
                                                            false,
                                                            true);
        InstallInteractor installInteractor = installSetUp.getInstallInteractor();
        List<InstallResult> listInstallResult = installInteractor.installPlugin(installInput);
        assertEquals(listInstallResult.size(), 4);

        InstallResult yasuiInstallResult = listInstallResult.get(0);
        assertEquals(yasuiInstallResult.name(),"JedCore");
        assertEquals(yasuiInstallResult.type(), InstallResult.Type.SUCCESS_INSTALLED);

        InstallResult NyaaCoreInstallResult = listInstallResult.get(1);
        assertEquals(NyaaCoreInstallResult.name(),"ProjectKorra");
        assertEquals(NyaaCoreInstallResult.type(), InstallResult.Type.SUCCESS_INSTALLED);

    }

    /**
     * Test case: Installing the (uninstalled) dependency should still go through
     * if the input plugin has already been installed
     */
    @Test
    public void test_InstallMissingDependency_OfInputPlugin() {
        InstallSetUp installSetUp = InstallSetUp.getInstaller();
        InstallInput installInput = new InstallInput("JedCore",
                SearchPackagesType.BY_NAME,
                false,
                true);
        InstallInteractor installInteractor = installSetUp.getInstallInteractor();
        installInteractor.installPlugin(installInput);
        // Assume the user doesn't like ProjectKorra dependency, and he/she/they uninstalled it
        File dependency = new File("plugins/ProjectKorra.jar");
        dependency.delete();

        List<InstallResult> listInstallResult = installInteractor.installPlugin(installInput);
        // Ensure that ProjectKorra is installed and each plugin has 2 result states (install & load)
        assertEquals(listInstallResult.size(), 4);

        InstallResult JedCoreInstallResult = listInstallResult.get(0);
        assertEquals(JedCoreInstallResult.name(),"JedCore");
        assertEquals(JedCoreInstallResult.type(), InstallResult.Type.PLUGIN_EXISTS);

        InstallResult ProjectKorraInstallResult = listInstallResult.get(1);
        assertEquals(ProjectKorraInstallResult.name(),"ProjectKorra");
        assertEquals(ProjectKorraInstallResult.type(), InstallResult.Type.SUCCESS_INSTALLED);
    }

}
