package org.hydev.mcpm.client.search;

import org.hydev.mcpm.client.models.PluginModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Searcher that returns a map based on names.
 *
 */
public class SearcherByName implements Searcher {

    private Map<String, List<PluginModel>> nameMap = null;

    /**
     * Returns a dictionary mapping the different names to the matching plugins.
     * Not case-sensitive.
     *
     * @param plugins A list of all plugins in the database.
     * @return A dictionary associating the name to the matching plugins.
     */
    public Map<String, List<PluginModel>> constructSearchMaps(List<PluginModel> plugins) {
        Map<String, List<PluginModel>> models = new HashMap<>();
        for (PluginModel plugin : plugins) {
            // Get latest version
            plugin.getLatest().ifPresent(p ->
            {
                if (p.meta() != null && p.meta().name() != null && !p.meta().name().isBlank()) {
                    String name = p.meta().name().toLowerCase();
                    if (!models.containsKey(name))
                        models.put(name, new ArrayList<>());
                    models.get(name).add(plugin);
                }
            });
        }
        return models;
    }

    /**
     * Searches for plugins based on the provided user input.
     *
     * @param inp User input for the search. Should be a name as a string here.
     * @param plugins A list of all plugins in the database.
     * @return A list of plugins associated to inp.
     */
    @Override
    public List<PluginModel> getSearchList(String inp, List<PluginModel> plugins) {
        // Instantiate if null
        if (nameMap == null) {
            nameMap = constructSearchMaps(plugins);
        }
        return nameMap.getOrDefault(inp, List.of());
    }
}
