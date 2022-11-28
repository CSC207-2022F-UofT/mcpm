package org.hydev.mcpm.client.database;

import org.apache.commons.lang3.NotImplementedException;
import org.hydev.mcpm.client.models.PluginYml;

import java.util.List;

/**
 * Implementation to the ListAll functionality
 */
public record ListAllInteractor(PluginTracker tracker) implements ListAllBoundary
{
    /**
     * listAllInteractor interacts with the LocalPluginTracker to get the list of plugins, according to a specified
     * parameter
     *
     * @param parameter The parameter for the ListAll use case. 'All' denotes a request to list all manually
     *     installed plugins, 'manual' denotes a request to list all manually installed plugins, and 'outdated' denotes
     *     a request to list all manually installed plugins that are outdated.
     */
    public List<PluginYml> listAll(String parameter)
    {
        var installed = tracker.listInstalled();
        switch (parameter)
        {
            case "all":
                return installed;

            case "manual":
                var local = tracker.listManuallyInstalled();
                return installed.stream().filter(it -> local.contains(it.name())).toList();

            case "outdated":
                throw new NotImplementedException("Method to print outdated plugins not implemented yet");

            default:
                return null;
        }
    }
}
