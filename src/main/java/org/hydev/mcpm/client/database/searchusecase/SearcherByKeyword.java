package org.hydev.mcpm.client.database.searchusecase;

import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;

import java.util.*;

/**
 * Searcher that returns a map based on keywords.
 *
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 */
public class SearcherByKeyword implements Searcher {

    /**
     * Returns a dictionary mapping the different keywords to the matching plugins
     * This function should ignore letter case in searching.
     * For example, searching "java" would match "Java" as well.
     * This function implements fuzzy search that doesn't require the exact phrase to be available,
     * but requires all words in the phrase to be present.
     * For example, searching "java 11" would match "java jdk 11" but not "java"
     * @param plugins A list of all plugins in the database.
     * @return A dictionary associating each keyword to the matching plugins.
     */
    @Override
    public Map<String, List<PluginModel>> constructSearchMaps(List<PluginModel> plugins) {
        Map<String, List<PluginModel>> models = new HashMap<>();
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
