package org.hydev.mcpm.client.database.inputs;

import org.hydev.mcpm.client.models.PluginModel;

import java.util.List;

/**
 * Result returned from ListPackagesBoundary.
 *
 * @param state The outcome of the request. Must be Success for the other values to be valid.
 * @param pageNumber The page number that was requested (same as input.pageNumber).
 * @param plugins The list of plugins on this page.
 * @param totalPlugins The total amount of plugins in the database.
 */
public record ListPackagesResult(
    State state,
    int pageNumber,
    List<PluginModel> plugins,
    int totalPlugins
) {
    public enum State {
        Success,
        InvalidInput,
        FailedToFetchDatabase,
        NoSuchPage,
    }

    public static ListPackagesResult by(State state) {
        return new ListPackagesResult(state, 0, List.of(), 0);
    }
}
