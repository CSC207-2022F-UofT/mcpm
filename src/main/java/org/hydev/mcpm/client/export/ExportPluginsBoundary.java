package org.hydev.mcpm.client.export;

/**
 * Defines how a user can export their plugins.
 */
public interface ExportPluginsBoundary {
    /**
     *
     * @param input The output stream to write to
     * @return Whether the export was successful or not
     */
    ExportPluginsResult export(ExportPluginsInput input);
}
