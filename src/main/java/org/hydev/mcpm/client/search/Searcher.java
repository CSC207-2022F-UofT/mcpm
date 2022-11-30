package org.hydev.mcpm.client.search;

import org.hydev.mcpm.client.models.PluginModel;

import java.util.List;
import java.util.Map;

/**
 * Interface for searchers that return Maps based on the list of plugins.
 *
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 */
public interface Searcher {
    /**
     * Constructs a dictionary associating a feature (e.g. name, keyword, command)
     * of the plugins to the matching plugins.
     *
     * @param plugins A list of all plugins in the database.
     * @return A dictionary associating a feature of the plugins to the matching plugins.
     */
    Map<String, List<PluginModel>> constructSearchMaps(List<PluginModel> plugins);

    /**
     * Searches for plugins based on the provided user input.
     *
     * @param inp User input for the search, as a string.
     * @param plugins A list of all plugins in the database.
     * @return A list of plugins based on inp.
     */
    List<PluginModel> getSearchList(String inp, List<PluginModel> plugins);
}
