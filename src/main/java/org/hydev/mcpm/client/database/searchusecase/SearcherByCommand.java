package org.hydev.mcpm.client.database.searchusecase;

import org.hydev.mcpm.client.models.PluginModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     *         Returns null if inp is not a string.
     */
    @Override
    public Map<String, List<PluginModel>> constructSearchMaps(List<PluginModel> plugins) {
        Map<String, List<PluginModel>> models = new HashMap<>();
        for (PluginModel plugin : plugins) {
            // Get latest version
            var v = plugin.getLatestPluginVersion();
            if (v.isPresent() && v.get().meta() != null && v.get().meta().commands() != null) {
                List<String> aliases = v.get().meta()
                        .commands().keySet().stream()
                        .map(String::toLowerCase).toList();
                for (String alias: aliases) {
                    if (!models.containsKey(alias))
                        models.put(alias, new ArrayList<>());
                    models.get(alias).add(plugin);
                }
            }
        }
        return models;
    }

    /**
     * Searches for plugins based on the command the user provides.
     *
     * @param inp User input for the search. Should be a name as a string here.
     * @param plugins A list of all plugins in the database.
     * @return A list of plugins based on inp.
     */
    @Override
    public List<PluginModel> getSearchList(String inp, List<PluginModel> plugins) {
        // Instantiate if null
        if (SearcherByCommand.commandMap == null) {
            SearcherByCommand.commandMap = constructSearchMaps(plugins);
        }
        return SearcherByCommand.commandMap.get(inp);
    }
}
