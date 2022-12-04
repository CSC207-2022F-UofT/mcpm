package org.hydev.mcpm.client.updater;

import org.hydev.mcpm.client.matcher.PluginVersionState;
import org.hydev.mcpm.client.models.PluginModel;

import java.util.Map;
import java.util.Set;

/**
 * Result returned from CheckForUpdatesBoundary.
 * If a plugin is in the mismatched list, you might want to ask the user if they can be removed.
 *
 * @param state The outcome of the request. Must be Success for the other values to be valid.
 * @param updatable A map from version ids to all plugins that require updates.
 * @param mismatched A list of all plugins that were not matched to any Plugin in the database.
 */
public record CheckForUpdatesResult(
    State state,
    Map<PluginVersionState, PluginModel> updatable,
    Set<PluginVersionState> mismatched
) {
    /**
     * The outcome of the CheckForUpdatesResult.
     */
    public enum State {
        SUCCESS,
        INVALID_INPUT,
        FAILED_TO_FETCH_DATABASE,
    }

    /**
     * Creates an empty CheckForUpdatesResult with the provided state.
     * The default values are provided in order to easily create objects with failure States.
     *
     * @param state The state of the CheckForUpdatesResult.
     * @return A CheckForUpdatesResult object with state initialized and dummy values for the other elements.
     */
    public static CheckForUpdatesResult by(CheckForUpdatesResult.State state) {
        return new CheckForUpdatesResult(state, Map.of(), Set.of());
    }
}
