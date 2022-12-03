package org.hydev.mcpm.client.updater;

import org.hydev.mcpm.client.matcher.PluginVersionState;

import java.util.List;

/**
 * Input for the CheckForUpdatesBoundary.
 *
 * @param states A list of all plugins that should be considered for updates.
 * @param noCache If true, the ListPackagesBoundary will re-download the database before performing the request.
 */
public record CheckForUpdatesInput(
    List<PluginVersionState> states,
    boolean noCache
) { }
