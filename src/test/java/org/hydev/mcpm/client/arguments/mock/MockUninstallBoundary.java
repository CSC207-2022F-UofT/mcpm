package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.uninstall.UninstallBoundary;
import org.hydev.mcpm.client.uninstall.UninstallInput;
import org.hydev.mcpm.client.uninstall.UninstallResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a mock implementation of the UninstallBoundary interface for testing.
 */
public class MockUninstallBoundary implements UninstallBoundary {
    private final List<UninstallInput> inputs = new ArrayList<>();
    private UninstallResult.State defaultState = UninstallResult.State.SUCCESS;

    @Override
    public UninstallResult uninstall(UninstallInput input) {
        inputs.add(input);

        return new UninstallResult(defaultState);
    }

    /**
     * Changes the default state returned for this uninstall result.
     * As this is a mock class, I'm not really reserved about adding mutator methods.
     * It adds some complexity in setup if I avoid this.
     *
     * @param state The state to set.
     */
    public void setDefaultState(UninstallResult.State state) {
        defaultState = state;
    }

    /**
     * Gets a list of all inputs that this interface was invoked with.
     *
     * @return A list of input objects.
     */
    public List<UninstallInput> getInputs() {
        return List.copyOf(inputs);
    }
}
