package org.hydev.mcpm.client.database.searchusecase;

import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;

import java.util.*;

public class SearcherByName implements Searcher {

    /**
     * Dynamically load a local plugin through JVM reflections and classloader hacks
     *
     * @param name Loaded plugin name
     * @return A list of PluginModels with the given name.
     */
//    @Override
//    public List<PluginModel> search(List<PluginModel> plugins, String name) {
//        List<PluginModel> models = new ArrayList<>();
//        for (PluginModel plugin : plugins) {
//            // Get latest version
//            var v = plugin.versions().stream().max(Comparator.comparingLong(PluginVersion::id));
//            if (v.isPresent())
//                if (v.get().meta().name().equals(name))
//                    models.add(plugin);
//        }
//        return models;
//    }

    /**
     * Returns a dictionary mapping the different names to the matching plugins.
     * Not case-sensitive.
     * @param plugins A list of all plugins in the database.
     * @return A dictionary associating the name to the matching plugins.
     */
    @Override
    public Map<String, List<PluginModel>> constructSearchMaps(List<PluginModel> plugins) {
        Map<String, List<PluginModel>> models = new HashMap<String, List<PluginModel>>();
        for (PluginModel plugin : plugins) {
            // Get latest version
            var v = plugin.versions().stream().max(Comparator.comparingLong(PluginVersion::id));
            if (v.isPresent()) {
                String name = v.get().meta().name().toLowerCase();
                if (!models.containsKey(name))
                    models.put(name, List.of());
                models.get(name).add(plugin);
            }
        }
        return models;
    }
}
