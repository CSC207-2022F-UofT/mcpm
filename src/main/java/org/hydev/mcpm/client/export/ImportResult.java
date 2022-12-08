package org.hydev.mcpm.client.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Class storing results of the import for each imported plugin
 */
public class ImportResult {
    private State state;
    private final Map<String, Boolean> installResults;
    public final List<String> noninstalledPlugins;
    public final String error;


    /**
     * Result for importing plugins
     *
     * @param installResults Result of each install
     */
    public ImportResult(Map<String, Boolean> installResults) {
        this.installResults = installResults;
        noninstalledPlugins = new ArrayList<>();
        error = null;

        boolean success = true;
        boolean fail = false;

        /* Code for when import returns ImportResult instead of boolean
        Set<Type> good = new HashSet<>();
        for (Type type : Type.values()) {
            if (type.name().contains("SUCCESS")) // definitely legit
                good.add(type);
        }
         */

        for (var x : installResults.entrySet()) {
            success &= x.getValue();
            fail |= x.getValue();

            if (!x.getValue()) {
                noninstalledPlugins.add(x.getKey());
            }

            if (!success && fail) { // not a full success nor full failure
                state = State.PARTIAL_SUCCESS;
            }
        }

        if (state == null)
            state = success ? State.SUCCESS : State.FAILURE;
    }

    /**
     * ImportResult constructor with the given error message
     *
     * @param error The error message
     */
    public ImportResult(String error) {
        this.error = error;
        state = State.IMPORT_ERROR;
        noninstalledPlugins = null;
        installResults = null;
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
