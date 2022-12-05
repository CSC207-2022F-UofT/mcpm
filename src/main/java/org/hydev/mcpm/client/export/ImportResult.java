package org.hydev.mcpm.client.export;

import org.hydev.mcpm.client.installer.InstallResult;

import java.util.EnumSet;
import java.util.Map;

/**
 * Result for importing plugins
 *
 * @param installResults Result of each install
 */
public record ImportResult(Map<String, InstallResult> installResults) {
    /**
     * State of a single import
     */
    public enum State {
        SUCCESS,
        PARTIAL_SUCCESS,
        FAILURE
    }

    /**
     * Infer the state from the state of the install results
     *
     * @return Overall state of the import
     */
    public State getState()
    {
        boolean success = true;
        boolean fail = false;

        /*
        EnumSet<InstallResult> = new RegularEnumSet<>();
        for (InstallResult x : installResults.values()) {
            success &= x.type();
            fail |= x.type();
        }
        */
        return State.SUCCESS;
    }
}
