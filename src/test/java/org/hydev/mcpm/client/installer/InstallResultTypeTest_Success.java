package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.installer.output.InstallResult;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class InstallResultTypeTest_Success {
    @Test
    public void testPluginInstalledWithDependency() {
        new File("plugins").mkdirs();
        InstallSetUp installSetUp = InstallSetUp.getInstaller();
        InstallInput installInput = new InstallInput("JedCore",
                                                            SearchPackagesType.BY_NAME,
                                                            false,
                                                            true);
        InstallInteractor installInteractor = installSetUp.getInstallInteractor();
        List<InstallResult> listInstallResult = installInteractor.installPlugin(installInput);
        InstallResult installResult = listInstallResult.get(0);
        assert (installResult.name() == "JedCore");
        assert (installResult.type() == InstallResult.Type.PLUGIN_EXISTS);
    }
}
