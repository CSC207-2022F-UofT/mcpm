package org.hydev.mcpm.client.database.boundary;

import org.hydev.mcpm.client.database.inputs.MatchPluginsInput;
import org.hydev.mcpm.client.database.inputs.MatchPluginsResult;

/**
 * Defines how a user can match plugins by an identifier.
 * This is planned to be used to fetch details about PluginModels
 * from the database when we have limited information
 * (ex. PluginLock only stores Plugin "names" for some reason).
 * This can also be used by CheckForUpdatesBoundary.
 */
public interface MatchPluginsBoundary {
    /**
     * Looks through the database for plugins that match the model id's in input.
     *
     * @param input A list of plugin identifiers that will be searched for in the database.
     * @return A map of all identifiers to the underlying PluginModel objects (if found).
     */
    MatchPluginsResult match(MatchPluginsInput input);
}
