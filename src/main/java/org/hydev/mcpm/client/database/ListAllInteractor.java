package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.models.PluginYml;

import java.util.List;

/**
 * Implementation to the ListAll functionality
 *
 * @author Kevin (https://github.com/kchprog)
 * @since 2022-11-20
 */
public class ListAllInteractor implements ListAllBoundary
{
    LocalPluginTracker localPluginTracker = new LocalPluginTracker();

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
        var installed = localPluginTracker.listInstalled();
        switch (parameter)
        {
            case "all":
                return installed;

            case "manual":
                var local = localPluginTracker.listManuallyInstalled();
                return installed.stream().filter(it -> local.contains(it.name())).toList();

            case "outdated":
                // TODO: Implement this
                throw new UnsupportedOperationException("TODO");

            default:
                return null;
        }
    }
}
