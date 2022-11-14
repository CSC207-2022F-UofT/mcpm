package org.hydev.mcpm.client.database.results;

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
    /**
     * The outcome of the ListPackagesResult.
     */
    public enum State {
        SUCCESS,
        INVALID_INPUT,
        FAILED_TO_FETCH_DATABASE,
        NO_SUCH_PAGE,
    }

    /**
     * Creates an empty ListPackagesResult with the provided state.
     * The default values are provided in order to easily create objects with failure States (ex. InvalidInput).
     *
     * @param state The state of the ListPackagesResult.
     * @return A ListPackagesResult object with the state property initialized and dummy values for the other elements.
     */
    public static ListPackagesResult by(State state) {
        return new ListPackagesResult(state, 0, List.of(), 0);
    }
}
