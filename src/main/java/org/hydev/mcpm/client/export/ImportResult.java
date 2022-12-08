package org.hydev.mcpm.client.export;

import org.hydev.mcpm.client.installer.output.InstallResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Class storing results of the import for each imported plugin
 */
public class ImportResult {
    private final State state;
    public final List<String> nonInstalledPlugins;
    public final String error;


    /**
     * Result for importing plugins
     *
     * @param installResults Result of each install
     */
    public ImportResult(List<InstallResult> installResults) {
        nonInstalledPlugins = new ArrayList<>();
        error = null;

        boolean allSuccessful = true;
        boolean allFailed = true;

        // Code for when import returns ImportResult instead of boolean
        Set<InstallResult.Type> good = Set.of(InstallResult.Type.SUCCESS_INSTALLED, InstallResult.Type.PLUGIN_EXISTS);
        for (var result : installResults) {
            if (result.name().equalsIgnoreCase("mcpm") ||
                    result.name().equalsIgnoreCase("mcpm-helper")) // ignore mcpm imports
                continue;

            boolean successfulInstall = good.contains(result.type());
            allSuccessful &= successfulInstall;
            allFailed &= !successfulInstall;

            if (!successfulInstall) {
                nonInstalledPlugins.add(result.name());
            }
        }
        if (allSuccessful) {
            state = State.SUCCESS;
        }
        else if (allFailed) {
            state = State.FAILURE;
        }
        else {
            state = State.PARTIAL_SUCCESS;
        }
    }

    /**
     * ImportResult constructor with the given error message
     *
     * @param error The error message
     */
    public ImportResult(String error) {
        this.error = error;
        state = State.IMPORT_ERROR;
        nonInstalledPlugins = null;
    }

    /**
     * Aggregate state of install
     */
    public enum State {
        SUCCESS,
        PARTIAL_SUCCESS,
        FAILURE,
        IMPORT_ERROR
    }

    /**
     * Infer the state from the state of the installation results
     *
     * @return Overall state of the import
     */
    public State state()
    {
        return state;
    }
}
