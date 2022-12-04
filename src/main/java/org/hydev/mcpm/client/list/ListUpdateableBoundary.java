package org.hydev.mcpm.client.list;

import java.util.ArrayList;
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.database.tracker.SuperPluginTracker;

/**
 * Defines an interface for obtaining a list of plugin names that may be updated
 */
public interface ListUpdateableBoundary {
    ArrayList<String> listUpdateable(SuperPluginTracker superPluginTracker,
            CheckForUpdatesBoundary checkForUpdatesBoundary);
}
