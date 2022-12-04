package org.hydev.mcpm.client.matcher;

/**
 * Used to present the current state of a plugin version for CheckForUpdatesBoundary.
 *
 * @param modelId An identifier for this plugin.
 *                Example `PluginModelId.byName("OwnPlots")`.
 * @param versionId An identifier for the version this plugin is currently at.
 *                  Example `PluginVersionId.byString("1.2.1")`.
 */
public record PluginVersionState(
    // No sum types.
    PluginModelId modelId,
    PluginVersionId versionId
) { }
