package org.hydev.mcpm.client.database.searchusecase;

import org.hydev.mcpm.client.models.PluginModel;

import java.util.*;

/**
 * Searcher that returns a map based on commands.
 *
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 */
public class SearcherByCommand implements Searcher {

    private static Map<String, List<PluginModel>> commandMap = null;

    /**
     * Returns a dictionary mapping the different commands to the matching plugins.
     *
     * @param plugins A list of all plugins in the database.
     * @return A dictionary associating the command to the matching plugins.
     */
    @Override
    public Map<String, List<PluginModel>> constructSearchMaps(List<PluginModel> plugins) {
        //TODO
        return new HashMap<>();
    }

    /**
     * Searches for plugins based on the provided user input.
     *
     * @param inp User input for the search. Should be a name as a string here.
     * @param plugins A list of all plugins in the database.
     * @return A dictionary associating a string feature of the plugins to the matching plugins.
     */
    @Override
    public List<PluginModel> getSearchList(Object inp, List<PluginModel> plugins) {
        // Todo
        return List.of();
    }
}
