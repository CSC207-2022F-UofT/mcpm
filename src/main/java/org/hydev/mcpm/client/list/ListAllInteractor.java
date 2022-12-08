package org.hydev.mcpm.client.list;

import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.database.tracker.PluginTracker;

import java.util.List;

/**
 * Implementation for the ListAll functionality
 */
public record ListAllInteractor(PluginTracker pluginTracker, CheckForUpdatesBoundary checkForUpdatesBoundary)
    implements ListAllBoundary {
    /**
     * listAllInteractor interacts with the LocalPluginTracker to get the list of
     * plugins, according to a specified
     * parameter
     *
     *
     * @param parameter The parameter for the ListAll use case. 'All' denotes a
     *                  request to list all manually
     *                  installed plugins, 'manual' denotes a request to list all
     *                  manually installed plugins, and 'outdated' denotes
     *                  a request to list all manually installed plugins that are
     *                  outdated.
     */
    public List<PluginYml> listAll(ListType parameter) {
        var installed = pluginTracker.listInstalled();
        switch (parameter) {
            case ALL -> {
                return installed;
            }
            case MANUAL -> {
                var local = pluginTracker.listManuallyInstalled();
                return installed.stream().filter(it -> local.contains(it.name())).toList();
            }
            case AUTOMATIC -> {
                var manual = pluginTracker.listManuallyInstalled();
                return installed.stream().filter(it -> !manual.contains(it.name())).toList();
            }
            case OUTDATED -> {
                ListUpdatableBoundary listUpdatableBoundary = new ListUpdatableHelper();
                var outdated = listUpdatableBoundary.listUpdatable(pluginTracker, checkForUpdatesBoundary);
                return installed.stream().filter(it -> outdated.contains(it.name())).toList();
            }
            default -> {
                return null;
            }
        }
    }
}
