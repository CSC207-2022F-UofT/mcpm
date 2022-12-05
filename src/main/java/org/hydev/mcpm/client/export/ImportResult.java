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
    public enum State {
        SUCCESS,
        PARTIAL_SUCCESS,
        FAILURE
    }

    public State getState()
    {
        boolean success = true;
        boolean fail = false;

        EnumSet<InstallResult> = new RegularEnumSet<>();
        for (InstallResult x : installResults.values()) {
            success &= x.type();
            fail |= x.type();
        }
    }
}
