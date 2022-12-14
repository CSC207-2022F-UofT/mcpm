
package org.hydev.mcpm.client.list;

import org.hydev.mcpm.client.models.PluginTrackerModel;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.matcher.PluginVersionState;
import org.hydev.mcpm.client.matcher.PluginModelId;
import org.hydev.mcpm.client.matcher.PluginVersionId;

import org.hydev.mcpm.client.updater.CheckForUpdatesResult;
import org.hydev.mcpm.client.updater.CheckForUpdatesInput;

import java.util.ArrayList;

import java.util.List;
import java.util.OptionalLong;

/**
 * A helper class for ListUpdatableBoundary.
 */
public class ListUpdatableHelper implements ListUpdatableBoundary {

    /**
     * Returns a list of plugins names that belong to outdated plugins
     *
     * @param localPluginTracker      The plugin tracker in question
     * @param checkForUpdatesBoundary The boundary to check for updates
     * @return A list of plugins that are outdated.
     */
    public ArrayList<String> listUpdatable(
        PluginTracker localPluginTracker, CheckForUpdatesBoundary checkForUpdatesBoundary) {

        ArrayList<PluginVersionState> temp = new ArrayList<>();
        List<PluginTrackerModel> installedModels = localPluginTracker.listEntries();

        // Generates a list of PluginVersionStates from the installed plugins obtained
        // from the
        // SuperLocalPluginTracker instance. Creates a valid input to
        // CheckForUpdatesBoundary

        for (PluginTrackerModel installedModel : installedModels) {
            PluginVersionId pluginVersionId = new PluginVersionId(
                    OptionalLong.of(installedModel.getVersionId()), null);

            PluginModelId pluginModelId = new PluginModelId(
                    OptionalLong.of(installedModel.getPluginId()), installedModel.getName(), null);

            try {
                temp.add(new PluginVersionState(pluginModelId, pluginVersionId));
            } catch (Exception e) {
                break;
            }
        }

        CheckForUpdatesInput input = new CheckForUpdatesInput(temp, false);
        CheckForUpdatesResult rawResult = checkForUpdatesBoundary.updates(input);

        // Read the list of installedModels and create a CheckForUpdatesInput object
        // with state equal
        // to the list of PluginTrackerModels' version

        if (rawResult.state() == CheckForUpdatesResult.State.SUCCESS) {

            // get the names of the plugins that are outdated from result
            ArrayList<String> outdated = new ArrayList<>();
            for (PluginModel pluginModel : rawResult.updatable().values()) {
                pluginModel.getLatestPluginVersion()
                    .ifPresent(pluginVersion -> outdated.add(pluginVersion.meta().name()));
            }
            return outdated;
        }

        return new ArrayList<>();
    }

}
