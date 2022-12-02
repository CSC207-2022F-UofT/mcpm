package org.hydev.mcpm.client.database.boundary;

import org.hydev.mcpm.client.database.inputs.CheckForUpdatesInput;
import org.hydev.mcpm.client.database.results.CheckForUpdatesResult;

/**
 * Defines how a user can search for plugin updates.
 */
public interface CheckForUpdatesBoundary {
    /**
     * Looks through the database for plugins in CheckForUpdatesInput that have an updated version.
     *
     * @param forInput A list of all plugins + plugin version identifiers that will be checked for updates.
     * @return A list of plugins that need updates in CheckForUpdatesResult#updatable.
     */
    CheckForUpdatesResult updates(CheckForUpdatesInput forInput);
}
