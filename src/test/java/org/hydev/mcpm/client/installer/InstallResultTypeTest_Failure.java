package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.installer.output.InstallResult;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Tests the Installing Result Type
 */
public class InstallResultTypeTest_Failure {

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
        assert (installResult.name() == "");
        assert (installResult.type() == InstallResult.Type.SEARCH_INVALID_INPUT);
    }

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
       assert (installResult.name() == "UwU");
       assert (installResult.type() == InstallResult.Type.NOT_FOUND);
    }

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
        assert (installResult.name() == "JedCore");
        assert (installResult.type() == InstallResult.Type.PLUGIN_EXISTS);
    }
}
