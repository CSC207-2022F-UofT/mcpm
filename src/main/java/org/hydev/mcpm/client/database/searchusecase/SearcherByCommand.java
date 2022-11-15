package org.hydev.mcpm.client.database.searchusecase;

import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearcherByCommand implements Searcher {

    /**
     * Returns a dictionary mapping the different commands to the matching plugins.
     *
     * @param plugins A list of all plugins in the database.
     * @return A dictionary associating the command to the matching plugins.
     */
    @Override
    public Map<String, List<PluginModel>> constructSearchMaps(List<PluginModel> plugins) {
        Map<String, List<PluginModel>> models = new HashMap<String, List<PluginModel>>();
        for (PluginModel plugin : plugins) {
            // Get latest version
            var v = plugin.versions().stream().max(Comparator.comparingLong(PluginVersion::id));
            if (v.isPresent()) {
                String[] keywords = v.get().meta().description().toLowerCase().split(" ");
                for (String keyword: keywords) {
                    if (!models.containsKey(keyword))
                        models.put(keyword, List.of());
                    models.get(keyword).add(plugin);
                }
            }
        }
        return models;
    }
}
