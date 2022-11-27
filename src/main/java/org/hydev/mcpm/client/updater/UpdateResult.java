package org.hydev.mcpm.client.updater;

import java.util.Map;

public record UpdateResult(
    State state,
    Map<String, UpdateOutcome> outcomes
) {
    public enum State {
        INTERNAL_ERROR,
        FAILED_TO_FETCH_DATABASE,
        SUCCESS
    }

    public static UpdateResult by(State state) {
        return new UpdateResult(state, Map.of());
    }
}
