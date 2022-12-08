package org.hydev.mcpm.client.search;

import org.hydev.mcpm.client.models.PluginModel;

import java.util.List;

/**
 * Interface for searchers that return Maps based on the list of plugins.
 *
 */
public interface Searcher {
    /**
     * Searches for plugins based on the provided user input.
     *
     * @param inp User input for the search, as a string.
     * @param plugins A list of all plugins in the database.
     * @return A list of plugins based on inp.
     */
    List<PluginModel> getSearchList(String inp, List<PluginModel> plugins);
}
