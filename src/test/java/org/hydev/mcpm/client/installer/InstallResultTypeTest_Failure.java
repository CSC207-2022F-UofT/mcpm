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
public class InstallResultTypeTest_Failure {

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
     * Test case: invalid input, this happends when input name is empty -> searchPackageresult is null
     */
    @Test
    public void testInvalidInput() {
        InstallSetUp installSetUp = InstallSetUp.getInstaller();
        InstallInput installInput = new InstallInput("",
                                                    SearchPackagesType.BY_NAME,
                                                    false,
                                                    true);
        InstallInteractor installInteractor = installSetUp.getInstallInteractor();
        List<InstallResult> listInstallResult = installInteractor.installPlugin(installInput);
        InstallResult installResult = listInstallResult.get(0);
        assertEquals(installResult.name(), "");
        assert (installResult.type() == InstallResult.Type.SEARCH_INVALID_INPUT);
    }

    /**
     * Test case: plugin not found, this happends when input name is rando and does
     *             not match with database
     */
    @Test
    public void testSearchPluginNotFound(){
        InstallSetUp installSetUp = InstallSetUp.getInstaller();
        InstallInput installInput = new InstallInput("UwU",
                                                            SearchPackagesType.BY_NAME,
                                                            false,
                                                            true);
       InstallInteractor installInteractor = installSetUp.getInstallInteractor();
       List<InstallResult> listInstallResult = installInteractor.installPlugin(installInput);
       InstallResult installResult = listInstallResult.get(0);
       assertEquals(installResult.name(), "UwU");
       assertEquals(installResult.type(), InstallResult.Type.NOT_FOUND);
    }

    /**
     * Test case: plugin shouldn't be installed if it already exists locally
     */
    @Test
    public void testPluginAlreadyInstalledLocally(){
        InstallSetUp installSetUp = InstallSetUp.getInstaller();
        InstallInput installInput = new InstallInput("JedCore",
                                                    SearchPackagesType.BY_NAME,
                                                    false,
                                                    true);
        InstallInteractor installInteractor = installSetUp.getInstallInteractor();
        installInteractor.installPlugin(installInput);
        var listInstallResult = installInteractor.installPlugin(installInput);

        InstallResult installResult = listInstallResult.get(0);
        assertEquals(installResult.name(), "JedCore");
        assertEquals(installResult.type(), InstallResult.Type.PLUGIN_EXISTS);
    }
}
