package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.boundary.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.database.boundary.ListPackagesBoundary;
import org.hydev.mcpm.client.database.boundary.MatchPluginsBoundary;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.ProgressBarFetcherListener;
import org.hydev.mcpm.client.database.inputs.CheckForUpdatesInput;
import org.hydev.mcpm.client.database.inputs.CheckForUpdatesResult;
import org.hydev.mcpm.client.database.inputs.ListPackagesInput;
import org.hydev.mcpm.client.database.inputs.ListPackagesResult;
import org.hydev.mcpm.client.database.inputs.MatchPluginsInput;
import org.hydev.mcpm.client.database.inputs.MatchPluginsResult;
import org.hydev.mcpm.client.database.model.PluginModelId;
import org.hydev.mcpm.client.database.model.PluginVersionState;
import org.hydev.mcpm.client.models.PluginModel;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles fetching and performing operations on the plugin database.
 */
public class DatabaseInteractor implements ListPackagesBoundary, MatchPluginsBoundary, CheckForUpdatesBoundary {
    private final DatabaseFetcher fetcher;
    private final DatabaseFetcherListener listener;

    /**
     * Creates a new database with the provided database fetcher.
     * Consider passing LocalDatabaseFetcher. Defaults to the ProgressBarFetcherListener.
     *
     * @param fetcher The fetcher that will be used to request the database object in boundary calls.
     */
    public DatabaseInteractor(DatabaseFetcher fetcher) {
        this(fetcher, new ProgressBarFetcherListener());
    }

    /**
     * Creates a new database with the provided fetcher and upload listener.
     *
     * @param fetcher The fetcher that will be used to request the database object in boundary calls.
     * @param listener The listener that will receives updates if the database is downloaded from the internet.
     */
    public DatabaseInteractor(DatabaseFetcher fetcher, DatabaseFetcherListener listener) {
        this.fetcher = fetcher;
        this.listener = listener;
    }

    /**
     * Lists all plugins in the database with pagination.
     *
     * @param input Provided input. See the ListPackagesInput record for more info.
     * @return Packages result. See the ListPackagesResult record for more info.
     */
    @Override
    public ListPackagesResult list(ListPackagesInput input) {
        var database = fetcher.fetchDatabase(!input.noCache(), listener);

        if (database == null) {
            return ListPackagesResult.by(ListPackagesResult.State.FAILED_TO_FETCH_DATABASE);
        }

        if (input.pageNumber() < 0) {
            return ListPackagesResult.by(ListPackagesResult.State.INVALID_INPUT);
        }

        var plugins = database.plugins();

        // Request all items.
        if (input.itemsPerPage() <= 0) {
            return new ListPackagesResult(
                ListPackagesResult.State.SUCCESS,
                input.pageNumber(),
                new ArrayList<>(plugins),
                database.plugins().size()
            );
        }

        var begin = input.itemsPerPage() * input.pageNumber();
        var end = Math.min(begin + input.itemsPerPage(), plugins.size());

        if (plugins.size() <= begin) {
            return ListPackagesResult.by(ListPackagesResult.State.NO_SUCH_PAGE);
        }

        var result = plugins.subList(begin, end);

        return new ListPackagesResult(
            ListPackagesResult.State.SUCCESS,
            input.pageNumber(),
            result,
            database.plugins().size()
        );
    }

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

        var result = match(matchInput);

        switch (result.state()) {
            case SUCCESS -> {
                var mismatchedSet = Set.copyOf(result.mismatched());
                var mismatched = forInput.states().stream()
                    .filter(state -> mismatchedSet.contains(state.modelId()))
                    .toList();

                var updatable = new HashMap<PluginVersionState, PluginModel>();

                for (var state : forInput.states()) {
                    var value = result.matched().get(state.modelId());

                    if (value == null) {
                        continue;
                    }

                    var optionalLatest = value.latest();

                    // guard let?
                    if (optionalLatest.isEmpty()) {
                        return null;
                    }

                    var latest = optionalLatest.get();

                    if (!state.versionId().matches(latest)) {
                        updatable.put(state, value);
                    }
                }

                return new CheckForUpdatesResult(CheckForUpdatesResult.State.SUCCESS, updatable, mismatched);
            }
            case INVALID_INPUT -> {
                return CheckForUpdatesResult.by(CheckForUpdatesResult.State.INVALID_INPUT);
            }
            case FAILED_TO_FETCH_DATABASE -> {
                return CheckForUpdatesResult.by(CheckForUpdatesResult.State.FAILED_TO_FETCH_DATABASE);
            }
        }

        return CheckForUpdatesResult.by(CheckForUpdatesResult.State.INVALID_INPUT);
    }

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
                .findFirst();

            if (match.isEmpty()) {
                unmatched.add(id);
            } else {
                matched.put(id, match.get());
            }
        }

        return new MatchPluginsResult(MatchPluginsResult.State.SUCCESS, matched, unmatched);
    }

    /**
     * Demo main method for DatabaseInteractor.
     *
     * @param args Arguments are ignored.
     */
    public static void main(String[] args) {
        var host = URI.create("http://mcpm.hydev.org");
        var fetcher = new LocalDatabaseFetcher(host);
        var database = new DatabaseInteractor(fetcher);

        var result = database.list(new ListPackagesInput(20, 0, true));

        if (result.state() != ListPackagesResult.State.SUCCESS) {
            System.out.println("Result Failed With State " + result.state().name());
            return;
        }

        System.out.println("Result (" + result.pageNumber() + " for " + result.plugins().size() + " plugins):");

        var text = result
            .plugins()
            .stream()
            .map(x -> x.versions().stream().findFirst().map(value -> value.meta().name()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(value -> "  " + value)
            .collect(Collectors.joining("\n"));

        System.out.println(text);
    }

}
