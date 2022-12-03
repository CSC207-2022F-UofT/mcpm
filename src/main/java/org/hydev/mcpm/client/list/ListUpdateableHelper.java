
package org.hydev.mcpm.client.list;

import org.hydev.mcpm.client.models.PluginTrackerModel;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.database.tracker.SuperPluginTracker;
import org.hydev.mcpm.client.matcher.PluginVersionState;
import org.hydev.mcpm.client.matcher.PluginModelId;
import org.hydev.mcpm.client.matcher.PluginVersionId;

import org.hydev.mcpm.client.updater.CheckForUpdatesResult;
import org.hydev.mcpm.client.updater.CheckForUpdatesInput;

import java.util.ArrayList;

import java.util.OptionalLong;

/**
 * A helper class for ListUpdateableBoundary.
 */
public class ListUpdateableHelper implements ListUpdateableBoundary {

    /**
     * Returns a list of plugins names that belong to outdated plugins
     *
     * @param localPluginTracker      The plugin tracker in question
     * @param checkForUpdatesBoundary The boundary to check for updates
     * @return A list of plugins that are outdated.
     */
    public ArrayList<String> listUpdateable(
            SuperPluginTracker localPluginTracker, CheckForUpdatesBoundary checkForUpdatesBoundary) {

        ArrayList<PluginVersionState> temp = new ArrayList<>();
        ArrayList<PluginTrackerModel> installedModels = localPluginTracker.listInstalledAsModels();

        // Generates a list of PluginVersionStates from the installed plugins obtained
        // from the
        // SuperLocalPluginTracker instance

        for (PluginTrackerModel installedModel : installedModels) {
            PluginVersionId pluginVersionId = new PluginVersionId(
                    OptionalLong.of(Long.parseLong(installedModel.getVersionId())), null);

            PluginModelId pluginModelId = new PluginModelId(
                    OptionalLong.of(Long.parseLong(installedModel.getPluginId())), installedModel.getName(), null);

            temp.add(new PluginVersionState(pluginModelId, pluginVersionId));
        }

        CheckForUpdatesInput input = new CheckForUpdatesInput(temp, false);

        // Read the list of installedModels and create a CheckForUpdatesInput object
        // with state equal
        // to the list of PluginTrackerModels's version

        CheckForUpdatesResult rawResult = checkForUpdatesBoundary.updates(input);

        if (rawResult.state() == CheckForUpdatesResult.State.SUCCESS) {
            ArrayList<String> outdatedNames = new ArrayList<>();

            // get the ids of the plugins that are outdated from result
            ArrayList<String> outdated = new ArrayList<>();
            for (PluginModel pluginModel : rawResult.updatable().values()) {
                outdated.add(pluginModel.getLatestPluginVersion().get().meta().name());
            }
            return outdatedNames;

            // filter the installed plugins by the outdated ids

        }

        return new ArrayList<>();
        // Need to associate the IDs of the outdated plugins with the installed plugin
        // YML files, and return all matches

    }

}
