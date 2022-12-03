package org.hydev.mcpm.client.database;

import org.apache.commons.lang3.NotImplementedException;
import org.hydev.mcpm.client.database.boundary.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.database.inputs.CheckForUpdatesInput;
import org.hydev.mcpm.client.database.inputs.CheckForUpdatesResult;
import org.hydev.mcpm.client.database.model.PluginVersionState;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.OptionalLong;

/**
 * Implementation to the ListAll functionality
 *
 * @author Kevin (https://github.com/kchprog)
 * @since 2022-11-20
 */
public record ListAllInteractor(PluginTracker tracker) implements ListAllBoundary {
    /**
     * listAllInteractor interacts with the LocalPluginTracker to get the list of
     * plugins, according to a specified
     * parameter
     *
     * @param parameter The parameter for the ListAll use case. 'All' denotes a
     *                  request to list all manually
     *                  installed plugins, 'manual' denotes a request to list all
     *                  manually installed plugins, and 'outdated' denotes
     *                  a request to list all manually installed plugins that are
     *                  outdated.
     */
    public List<PluginYml> listAll(String parameter, CheckForUpdatesBoundary checkForUpdatesBoundary) {
        var installed = tracker.listInstalled();
        switch (parameter) {
            case "all":
                return installed;

            case "manual":
                var local = localPluginTracker.listManuallyInstalled();
                return installed.stream().filter(it -> local.contains(it.name())).toList();

            case "outdated":
                /*
                 * ArrayList<PluginVersionState> temp = new ArrayList<>();
                 * ArrayList<PluginTrackerModel> installedModels =
                 * localPluginTracker.listInstalledAsModels();
                 * 
                 * for (PluginTrackerModel installedModel : installedModels) {
                 * PluginVersionId pluginVersionId = new PluginVersionId(
                 * OptionalLong.of(Long.parseLong(installedModel.getVersionId())), null);
                 * 
                 * PluginModelId pluginModelId = new PluginModelId(
                 * OptionalLong.of(Long.parseLong(installedModel.getPluginId())),
                 * installedModel.getName(),
                 * null);
                 * 
                 * temp.add(new PluginVersionState(pluginModelId, pluginVersionId));
                 * }
                 * 
                 * CheckForUpdatesInput input = new CheckForUpdatesInput(temp, false);
                 * 
                 * // Read the list of installedModels and create a CheckForUpdatesInput object
                 * // with state equal
                 * // to the list of PluginTrackerModels's version
                 * 
                 * CheckForUpdatesResult rawResult = checkForUpdatesBoundary.updates(input);
                 * 
                 * if (rawResult.state() == CheckForUpdatesResult.State.SUCCESS) {
                 * Collection<PluginModel> result = rawResult.updatable().values();
                 * 
                 * // get the ids of the plugins that are outdated from result
                 * ArrayList<String> outdated = new ArrayList<>();
                 * for (PluginModel pluginModel : result) {
                 * outdated.add(pluginModel.id() + "");
                 * }
                 * 
                 * // filter the installed plugins by the outdated ids
                 * 
                 * }
                 * // Need to associate the IDs of the outdated plugins with the installed
                 * plugin YML files, and return all matches
                 */
                throw new NotImplementedException("Not implemented yet");

            default:
                return null;
        }
    }
}
