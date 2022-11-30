package org.hydev.mcpm.client.list;


import org.hydev.mcpm.client.models.PluginYml;

import java.util.List;

/**
 * Interface for listing all plugins
 */
public interface ListAllBoundary {

    /**
     * listAllInteractor interacts with the LocalPluginTracker to get the list of plugins, according to a specified
     * parameter
     *
     * @param parameter The parameter for the ListAll use case. 'All' denotes a request to list all manually
     *     installed plugins, 'manual' denotes a request to list all manually installed plugins, and 'outdated' denotes
     *     a request to list all manually installed plugins that are outdated.
     */
    List<PluginYml> listAll(String parameter);
}
