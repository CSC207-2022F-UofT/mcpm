package org.hydev.mcpm.client.database;

import org.hydev.mcpm.*;
import org.hydev.mcpm.client.models.PluginYml;

import java.util.ArrayList;
import java.util.List;

public class ListAllInteractor {
    LocalPluginTracker localPluginTracker = new LocalPluginTracker();

    public List<String> listAll(String parameter) {
        try {
            switch (parameter) {
                case "all":
                    List<PluginYml> pluginList = localPluginTracker.listInstalled();
                    List<String> pluginNames = pluginYmlListToString(pluginList);
                    return pluginNames;
                
                case "manual":
                    List<String> manualList = localPluginTracker.listManuallyInstalled();
                    return manualList;
                case "outdated":
                    return null;
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private List<String> pluginYmlListToString (List<PluginYml> pluginYmlList) {
        List<String> pluginNames = new ArrayList<String>();
        for (PluginYml plugin : pluginYmlList) {
            pluginNames.add(plugin.name());
        }
        return pluginNames;
    }
}
