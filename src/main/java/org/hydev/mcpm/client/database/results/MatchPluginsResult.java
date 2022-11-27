package org.hydev.mcpm.client.database.results;

import org.hydev.mcpm.client.database.model.PluginModelId;
import org.hydev.mcpm.client.models.PluginModel;

import java.util.List;
import java.util.Map;

/**
 * Result returned from CheckForUpdatesBoundary.
 * If a plugin is in the mismatched list, you might want to ask the user if they can be removed.
 *
 * @param state The outcome of the request. Must be Success for the other values to be valid.
 * @param matched A map from version ids to all plugins that match the condition (e.g. needs updates).
 * @param mismatched A list of all plugins that were not matched to any Plugin in the database.
 */
public record MatchPluginsResult(
    State state,
    Map<PluginModelId, PluginModel> matched,
    List<PluginModelId> mismatched
) {
    /**
     * The outcome of the MatchPluginsInput.
     */
    public enum State {
        SUCCESS,
        INVALID_INPUT,
        FAILED_TO_FETCH_DATABASE,
    }

    /**
     * Creates an empty MatchPluginsResult with the provided state.
     * The default values are provided in order to easily create objects with failure States.
     *
     * @param state The state of the MatchPluginsResult.
     * @return A MatchPluginsResult object with state initialized and dummy values for the other elements.
     */
    public static MatchPluginsResult by(MatchPluginsResult.State state) {
        return new MatchPluginsResult(state, Map.of(), List.of());
    }
}
