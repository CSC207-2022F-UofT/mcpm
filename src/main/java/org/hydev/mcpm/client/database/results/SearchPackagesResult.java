package org.hydev.mcpm.client.database.results;

import org.hydev.mcpm.client.models.PluginModel;
import java.util.List;

/**
 * Result returned from SearchPackagesBoundary.
 *
 * @param state The outcome of the request. Must be Success for the other values to be valid.
 * @param plugins The list of plugins corresponding to the search.
 *
 * @author Jerry Zhu (https://github.com/jerryzhu509)
 */
public record SearchPackagesResult(SearchPackagesResult.State state, List<PluginModel> plugins) {
    /**
     * The outcome of the SearchPackagesResult.
     */
    public enum State {
        SUCCESS,
        INVALID_INPUT,
        FAILED_TO_FETCH_DATABASE,
        INVALID_SEARCH_TYPE
    }

    /**
     * Creates an empty/default SearchPackagesResult with the provided state.
     * The default values are provided in order to easily create objects with failure States (ex. InvalidInput).
     *
     * @param state The state of the SearchPackagesResult.
     * @return A SearchPackagesResult object with the state property initialized and an empty list of plugins.
     */
    public static SearchPackagesResult by(SearchPackagesResult.State state) {
        return new SearchPackagesResult(state, List.of());
    }
}
