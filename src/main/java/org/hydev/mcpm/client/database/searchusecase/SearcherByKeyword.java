package org.hydev.mcpm.client.database.searchusecase;

import org.hydev.mcpm.client.models.PluginModel;

import java.util.*;

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
                for (String keyword : keywords) {
                    if (!models.containsKey(keyword))
                        models.put(keyword, new ArrayList<>());
                    models.get(keyword).add(plugin);
                }
            }
        }
        return models;
    }

    /**
     * Searches for plugins based on the provided user input.
     *
     * @param inp User input for the search. Should be a name as a non-empty string here.
     * @param plugins A list of all plugins in the database.
     * @return A dictionary associating a string feature of the plugins to the matching plugins.
     *         Returns null if inp is not a string.
     */
    @Override
    public List<PluginModel> getSearchList(Object inp, List<PluginModel> plugins) {

        // Instantiate if null
        if (SearcherByKeyword.keywordMap == null) {
            SearcherByKeyword.keywordMap = constructSearchMaps(plugins);
        }
        if (!(inp instanceof String input))
            return null;
        String [] keywords = input.toLowerCase().split(" "); // Should be a string
        Set<PluginModel> res = new HashSet<>(SearcherByKeyword.keywordMap.get(keywords[0]));
        for (int i = 1; i < keywords.length; i++) {
            List<PluginModel> pl = SearcherByKeyword.keywordMap.get(keywords[i]);
            res.retainAll(pl);
        }
        return new ArrayList<>(res);
    }
}
