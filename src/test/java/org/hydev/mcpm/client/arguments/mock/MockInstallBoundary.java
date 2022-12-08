package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.input.FuzzyInstallInput;
import org.hydev.mcpm.client.installer.output.InstallResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a mock implementation of the InstallBoundary interface for testing.
 */
public class MockInstallBoundary implements InstallBoundary {
    private final List<FuzzyInstallInput> inputs = new ArrayList<>();

    @Override
    public List<InstallResult> installPlugin(FuzzyInstallInput installInput) {
        inputs.add(installInput);

        return List.of(new InstallResult(InstallResult.Type.SUCCESS_INSTALLED, installInput.name()));
    }

    /**
     * Gets a list of all inputs that this interface was invoked with.
     *
     * @return A list of input objects.
     */
    public List<FuzzyInstallInput> getInputs() {
        return List.copyOf(inputs);
    }
}
