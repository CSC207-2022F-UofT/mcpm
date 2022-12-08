package org.hydev.mcpm.client.export;

/**
 * Input for the ExportPluginsBoundary boundary.
 *
 * @param type The type of export
 * @param out String describing what to write to
 */
public record ExportPluginsInput(
        String type,
        String out
) {
}