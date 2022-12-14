package org.hydev.mcpm.client.matcher;

import org.hydev.mcpm.client.matcher.MatchPluginsBoundary;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.matcher.MatchPluginsInput;
import org.hydev.mcpm.client.matcher.PluginModelId;
import org.hydev.mcpm.client.matcher.MatchPluginsResult;
import org.hydev.mcpm.client.models.PluginModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * An interactor to match plugin identifiers to their names.
 *
 * @param fetcher A database fetcher to get database information.
 * @param listener A listener object to show database fetch progress.
 */
public record MatchPluginsInteractor(
    DatabaseFetcher fetcher,
    DatabaseFetcherListener listener
) implements MatchPluginsBoundary {
    /**
     * Checks for plugins that match the provided identifiers.
     *
     * @param input A list of plugin identifiers that will be searched for in the database.
     * @return A list of plugins that match the identifiers in MatchPluginsResult#matched.
     */
    @Override
    public MatchPluginsResult match(MatchPluginsInput input) {
        // Fail fast.
        var validState = input.pluginIds().stream()
            .allMatch(PluginModelId::valid);

        if (!validState) {
            return MatchPluginsResult.by(MatchPluginsResult.State.INVALID_INPUT);
        }

        var database = fetcher.fetchDatabase(!input.noCache(), listener);

        if (database == null) {
            return MatchPluginsResult.by(MatchPluginsResult.State.FAILED_TO_FETCH_DATABASE);
        }

        var matched = new HashMap<PluginModelId, PluginModel>();
        var unmatched = new ArrayList<PluginModelId>();

        for (var id : input.pluginIds()) {
            var match = database
                .plugins()
                .stream()
                .filter(id::matches)
                .max(Comparator.comparingLong(PluginModel::id)); // Latest

            if (match.isEmpty()) {
                unmatched.add(id);
            } else {
                matched.put(id, match.get());
            }
        }

        return new MatchPluginsResult(MatchPluginsResult.State.SUCCESS, matched, unmatched);
    }
}
