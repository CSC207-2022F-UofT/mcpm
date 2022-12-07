package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.installer.output.InstallResult;

import java.util.ArrayList;
import java.util.List;

public class MockInstallBoundary implements InstallBoundary {
    private final List<InstallInput> inputs = new ArrayList<>();

    @Override
    public List<InstallResult> installPlugin(InstallInput installInput) {
        inputs.add(installInput);

        return List.of(new InstallResult(InstallResult.Type.SUCCESS_INSTALLED, installInput.name()));
    }

    public List<InstallInput> getInputs() {
        return List.copyOf(inputs);
    }
}
