package org.hydev.mcpm.client.database.inputs;

import org.hydev.mcpm.client.database.model.PluginModelId;

import java.util.List;

/**
 * Input for the MatchPluginsInput.
 *
 * @param pluginIds A list of all plugins that should be considered for updates.
 * @param noCache If true, the ListPackagesBoundary will re-download the database before performing the request.
 */
public record MatchPluginsInput(
    List<PluginModelId> pluginIds,
    boolean noCache
) { }
