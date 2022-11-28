package org.hydev.mcpm.client.updater;

import java.util.Map;

/**
 * Information on the outcome of an update command.
 *
 * @param state The result of the update command.
 *              SUCCESS is required to have vaild values for other fields.
 * @param outcomes A map mapping plugin names to UpdateOutcome objects detailing whether they were updated.
 */
public record UpdateResult(
    State state,
    Map<String, UpdateOutcome> outcomes
) {
    /**
     * The result of the update command.
     */
    public enum State {
        INTERNAL_ERROR,
        FAILED_TO_FETCH_DATABASE,
        SUCCESS
    }

    /**
     * Provides dummy values of UpdateResult for various states.
     *
     * @param state The state of the returned UpdateResult.
     * @return A UpdateResult object.
     */
    public static UpdateResult by(State state) {
        return new UpdateResult(state, Map.of());
    }
}
