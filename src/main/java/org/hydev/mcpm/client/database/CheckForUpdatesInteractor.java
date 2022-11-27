package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.boundary.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.database.boundary.MatchPluginsBoundary;
import org.hydev.mcpm.client.database.inputs.CheckForUpdatesInput;
import org.hydev.mcpm.client.database.inputs.MatchPluginsInput;
import org.hydev.mcpm.client.database.model.PluginVersionState;
import org.hydev.mcpm.client.database.results.CheckForUpdatesResult;
import org.hydev.mcpm.client.database.results.MatchPluginsResult;
import org.hydev.mcpm.client.models.PluginModel;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public record CheckForUpdatesInteractor(
    MatchPluginsBoundary matchBoundary
) implements CheckForUpdatesBoundary {
    /**
     * Checks for plugin updates.
     *
     * @param forInput A list of all plugins + plugin version identifiers that will be checked for updates.
     * @return A list of plugins that needs updates in CheckForUpdatesResult#updates.
     */
    @Override
    public CheckForUpdatesResult updates(CheckForUpdatesInput forInput) {
        var isVersionsValid = forInput.states().stream()
            .allMatch(state -> state.versionId().valid());

        if (!isVersionsValid) {
            return CheckForUpdatesResult.by(CheckForUpdatesResult.State.INVALID_INPUT);
        }

        var matchInput = new MatchPluginsInput(
            forInput.states().stream().map(PluginVersionState::modelId).toList(),
            forInput.noCache()
        );

        var result = matchBoundary.match(matchInput);

        return switch (result.state()) {
            case SUCCESS ->
                filterUpdatablePlugins(forInput, result);

            case INVALID_INPUT ->
                CheckForUpdatesResult.by(CheckForUpdatesResult.State.INVALID_INPUT);

            case FAILED_TO_FETCH_DATABASE ->
                CheckForUpdatesResult.by(CheckForUpdatesResult.State.FAILED_TO_FETCH_DATABASE);
        };
    }

    private static CheckForUpdatesResult filterUpdatablePlugins(CheckForUpdatesInput forInput,
                                                                MatchPluginsResult result) {
        var mismatchedSet = Set.copyOf(result.mismatched());
        var mismatched = forInput.states().stream()
            .filter(state -> mismatchedSet.contains(state.modelId()))
            .collect(Collectors.toSet());

        var updatable = new HashMap<PluginVersionState, PluginModel>();

        for (var state : forInput.states()) {
            var value = result.matched().getOrDefault(state.modelId(), null);

            if (value == null) {
                continue;
            }

            var optionalLatest = value.getLatestPluginVersion();

            // guard let?
            if (optionalLatest.isEmpty()) {
                continue;
            }

            var latest = optionalLatest.get();

            if (!state.versionId().matches(latest)) {
                updatable.put(state, value);
            }
        }

        return new CheckForUpdatesResult(CheckForUpdatesResult.State.SUCCESS, updatable, mismatched);
    }
}
