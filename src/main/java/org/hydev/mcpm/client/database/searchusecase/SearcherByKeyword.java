package org.hydev.mcpm.client.database.searchusecase;

import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;

import java.util.*;

public class SearcherByKeyword implements Searcher {

//    /**
//     * Search for a plugin by keyword.
//     * <p>
//     * This function should ignore letter case in searching.
//     * For example, searching "java" would match "Java" as well
//     * <p>
//     * This function implements fuzzy search that doesn't require the exact phrase to be available,
//     * but requires all words in the phrase to be present.
//     * For example, searching "java 11" would match "java jdk 11" but not "java"
//     *
//     * @param keyword Keyword
//     * @return List of packages matching the keyword, or empty list
//     */
//    @Override
//    public List<PluginModel> search(List<PluginModel> plugins, String keyword) {
//        List<PluginModel> models = new ArrayList<>();
//        for (PluginModel plugin : plugins) {
//            // Get latest version
//            var v = plugin.versions().stream().max(Comparator.comparingLong(PluginVersion::id));
//            if (v.isPresent())
//                if (v.get().meta().description().equals(keyword))
//                    models.add(plugin);
//        }
//        return models;
//    }

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
