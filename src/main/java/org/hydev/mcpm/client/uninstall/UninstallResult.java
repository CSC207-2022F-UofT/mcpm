package org.hydev.mcpm.client.uninstall;

import java.util.HashMap;
import java.util.Map;

/**
 * Result for uninstalling
 *
 * @param state State of uninstall
 * @param dependencies States for uninstall dependencies
 * @author Anushka (https://github.com/aanushkasharma)
 * @since 2022-11-27
 */
public record UninstallResult(State state, Map<String, State> dependencies) {
    public UninstallResult(State state) {
        this(state, new HashMap<>());
    }

    /**
     * Result state for uninstall
     */
    public enum State {
        NOT_FOUND,
        FAILED_TO_DELETE,
        SUCCESS
    }
}
