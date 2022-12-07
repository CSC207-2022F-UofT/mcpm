package org.hydev.mcpm.client.export;

import java.util.Map;


/**
 * Class storing results of the
 */
public class ImportResult {
    private final State state;
    private final Map<String, Boolean> installResults;

    /**
     * Result for importing plugins
     *
     * @param installResults Result of each install
     */
    public ImportResult(Map<String, Boolean> installResults) {
        this.installResults = installResults;
        boolean success = true;
        boolean fail = false;

        /* Code for when import returns ImportResult instead of boolean
        Set<Type> good = new HashSet<>();
        for (Type type : Type.values()) {
            if (type.name().contains("SUCCESS")) // definitely legit
                good.add(type);
        }
         */

        for (var x : installResults.values()) {
            success &= x;
            fail |= x;
            if (!success && fail) { // not a full success nor full failure
                state = State.PARTIAL_SUCCESS;
                return;
            }
        }
        state = success ? State.SUCCESS : State.FAILURE;
    }


    /**
     * State of a single import
     */
    public enum State {
        SUCCESS,
        PARTIAL_SUCCESS,
        FAILURE
    }

    /**
     * Infer the state from the state of the installation results
     *
     * @return Overall state of the import
     */
    public State getState()
    {
        return state;
    }
}
