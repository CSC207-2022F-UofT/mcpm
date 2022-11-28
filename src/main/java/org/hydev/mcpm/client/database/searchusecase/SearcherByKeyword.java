package org.hydev.mcpm.client.database.searchusecase;

import org.hydev.mcpm.client.models.PluginModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Searcher that returns a map based on keywords.
 *
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 */
public class SearcherByKeyword implements Searcher {

    private static Map<String, List<PluginModel>> keywordMap = null;

    /**
     * Returns a dictionary mapping the different keywords to the matching plugins
     * This function should ignore letter case in searching.
     * For example, searching "java" would match "Java" as well.
     * This function implements fuzzy search that doesn't require the exact phrase to be available,
     * but requires all words in the phrase to be present.
     * For example, searching "java 11" would match "java jdk 11" but not "java"
     *
     * @param plugins A list of all plugins in the database.
     * @return A dictionary associating each keyword to the matching plugins.
     */
    @Override
    public Map<String, List<PluginModel>> constructSearchMaps(List<PluginModel> plugins) {
        Map<String, List<PluginModel>> models = new HashMap<>();
        for (PluginModel plugin : plugins) {
            // Get latest version
            var v = plugin.getLatestPluginVersion();
            if (v.isPresent() && v.get().meta() != null &&
                    v.get().meta().description() != null && !v.get().meta().description().equals("")) {
                String[] keywords = v.get().meta().description()
                        .replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
                for (String keyword : new HashSet<>(List.of(keywords))) {
                    if (!models.containsKey(keyword))
                        models.put(keyword, new ArrayList<>());
                    models.get(keyword).add(plugin);
                }
            }
        }
        return models;
    }

    /**
     * Searches for plugins based on the keywords the user provides.
     *
     * @param inp User input for the search. Should be a name as a non-empty string here.
     * @param plugins A list of all plugins in the database.
     * @return A list of plugins associated to inp.
     */
    @Override
    public List<PluginModel> getSearchList(String inp, List<PluginModel> plugins) {

        // Instantiate if null
        if (SearcherByKeyword.keywordMap == null) {
            SearcherByKeyword.keywordMap = constructSearchMaps(plugins);
        }
        String [] keywords = inp.split(" "); // Should be a string
        Set<PluginModel> res = new HashSet<>(SearcherByKeyword.keywordMap.getOrDefault(keywords[0], List.of()));
        for (int i = 1; i < keywords.length; i++) {
            List<PluginModel> pl = SearcherByKeyword.keywordMap.getOrDefault(keywords[i], List.of());
            res.retainAll(pl);
            if (res.isEmpty())
                return List.of();
        }
        return new ArrayList<>(res);
    }
}
