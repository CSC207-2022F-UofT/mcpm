package org.hydev.mcpm.client.export;

/**
 * Input for the ExportPluginsBoundary boundary.
 *
 * @param pastebin Link to the pastebin to write to
 */
public record ExportPluginsInput(
        String pastebin
) {
}