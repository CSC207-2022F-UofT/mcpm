package org.hydev.mcpm.client.updater;

import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.installer.IInstaller;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.matcher.PluginModelId;
import org.hydev.mcpm.client.matcher.PluginVersionId;
import org.hydev.mcpm.client.matcher.PluginVersionState;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.hydev.mcpm.utils.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hydev.mcpm.client.updater.UpdateOutcome.State.*;

/**
 * Handles update requests (installing, etc.)
 *
 * @param checkBoundary The boundary to use to check for plugin updates.
 * @param installer The boundary to use for installing new plugins.
 * @param pluginTracker The plugin tracker to use for getting installed plugin information.
 */
public record UpdateInteractor(
    CheckForUpdatesBoundary checkBoundary,
    IInstaller installer,
    PluginTracker pluginTracker
) implements UpdateBoundary {
    @Nullable
    private PluginVersionState stateByName(Map<String, PluginYml> installed, String name) {
        var version = installed.get(name);

        if (version == null)
            return null;

        return new PluginVersionState(
            PluginModelId.byName(name),
            PluginVersionId.byString(version.version())
        );
    }

    // This is hacky. It would be nice if we could look up states by name in update() so we return a map.
    private Map<String, PluginVersionState> stateMapByNames(List<String> names) {
        //
        var installed = pluginTracker.listInstalled().stream().map(it -> new Pair<>(it.name(), it))
            .collect(Pair.toMap());

        return names.stream()
            .map(name -> Pair.of(name, stateByName(installed, name)))
            .filter(pair -> pair.getValue() != null)
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private UpdateOutcome defaultOutcomeFor(@Nullable PluginVersionState version, UpdateOutcome.State state) {
        return new UpdateOutcome(
            state,
            version != null ? version.versionId().versionString() : null,
            null
        );
    }

    private UpdateOutcome updateByModel(PluginModel model, PluginVersionState state, boolean load) {
        var latest = model.getLatest().orElse(null);

        if (latest == null || latest.meta() == null || latest.meta().name() == null) {
            return defaultOutcomeFor(state, MISMATCHED);
        }

        var name = latest.meta().name();
        var latestVersion = latest.meta().version();

        // We should be able to assume state.versionId().versionString() != latestVersion

        // Please, PluginTracker cache your stuff!
        var manuallyInstalled = pluginTracker.listManuallyInstalled().contains(name);

        // Install Boundary shouldn't worry about this entry when installing.
        // We should assume InstallBoundary has the same PluginTracker.
        pluginTracker.removeEntry(name);

        var input = new InstallInput(name, SearchPackagesType.BY_NAME, load, manuallyInstalled);

        var result = installer.installPlugin(input);

        // Network error is used for events that aren't necessarily network errors.
        // But I don't want to have too many fail states. Maybe we should go for INTERNAL_ERROR?
        return switch (result.get(0).type()) {
            case NOT_FOUND -> defaultOutcomeFor(state, MISMATCHED);
            case SEARCH_INVALID_INPUT -> throw new RuntimeException(); // Something went wrong.
            case SEARCH_FAILED_TO_FETCH_DATABASE,
                NO_VERSION_AVAILABLE -> defaultOutcomeFor(state, NETWORK_ERROR);
            case PLUGIN_EXISTS -> throw new RuntimeException(); // We need to know something went wrong.
            case SUCCESS_INSTALLED -> new UpdateOutcome(UPDATED, state.versionId().versionString(), latestVersion);
        };
    }

    private UpdateOutcome makeOutcome(@Nullable PluginVersionState state, CheckForUpdatesResult result, boolean load) {
        // E.g. was filtered in stateMapByNames since there was no associated version.
        if (state == null) {
            return defaultOutcomeFor(null, NOT_INSTALLED);
        }

        if (result.mismatched().contains(state)) {
            return defaultOutcomeFor(state, MISMATCHED);
        } else {
            // We should know if updatable does not contain state, fail here.
            var model = result.updatable().getOrDefault(state, null);

            if (model == null) {
                return defaultOutcomeFor(state, UP_TO_DATE);
            }

            return updateByModel(model, state, load);
        }
    }

    @Override
    public UpdateResult update(UpdateInput input) {
        var pluginNames = input.pluginNames();

        if (pluginNames.isEmpty()) {
            // This means non-manually installed plugins will show up on the board.
            // I think that's desired behaviour.
            pluginNames = pluginTracker.listInstalled().stream().map(PluginYml::name).toList();
        }

        var states = stateMapByNames(pluginNames);
        var stateList = states.values().stream().toList();

        var checkInput = new CheckForUpdatesInput(stateList, input.noCache());
        var checkResult = checkBoundary.updates(checkInput);

        switch (checkResult.state()) {
            case SUCCESS -> { /* nothing */ }
            case INVALID_INPUT -> { return UpdateResult.by(UpdateResult.State.INTERNAL_ERROR); }
            case FAILED_TO_FETCH_DATABASE ->  { return UpdateResult.by(UpdateResult.State.FAILED_TO_FETCH_DATABASE); }
            default -> throw new RuntimeException(); // Handle this!
        }

        Map<String, UpdateOutcome> results = new HashMap<>();

        for (String name : pluginNames) {
            var state = states.getOrDefault(name, null); // This better succeed.

            results.put(name, makeOutcome(state, checkResult, input.load()));
        }

        return new UpdateResult(UpdateResult.State.SUCCESS, results);
    }
}
