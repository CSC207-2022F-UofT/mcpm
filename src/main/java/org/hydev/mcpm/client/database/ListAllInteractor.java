package org.hydev.mcpm.client.database;

import org.apache.commons.lang3.NotImplementedException;
import org.hydev.mcpm.*;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.client.database.LocalPluginTracker;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ListAllInteractor {
    LocalPluginTracker localPluginTracker = new LocalPluginTracker();

    /**
     * listAllInteractor interacts with the LocalPluginTracker to get the list of
     * plugins, according to a specified parameter
     * 
     * @param parameter The parameter for the ListAll use case. 'All' denotes a
     *                  request to list all manually installed plugins,
     *                  'manual' denotes a request to list all manually installed
     *                  plugins,
     *                  and 'outdated' denotes a request to list all manually
     *                  installed plugins that are outdated.
     * @throws Exception
     */
    public List<String> listAll(String parameter) {
        switch (parameter) {
            case "all":
                List<PluginYml> pluginList = localPluginTracker.listInstalled();
                return pluginList.stream().map(PluginYml::name).toList();

            case "manual":
                return localPluginTracker.listManuallyInstalled();

            case "outdated":
                throw new NotImplementedException("Method to print outdated plugins not implemented yet");

            default:
                return null;
        }
    }

    private List<String> pluginYmlListToString(List<PluginYml> pluginYmlList) {
        List<String> pluginNames = new ArrayList<String>();
        for (PluginYml plugin : pluginYmlList) {
            pluginNames.add(plugin.name());
        }
        return pluginNames;
    }
}
