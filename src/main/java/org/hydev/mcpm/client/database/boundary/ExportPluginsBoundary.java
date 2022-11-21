package org.hydev.mcpm.client.database.boundary;

import org.hydev.mcpm.client.database.inputs.ExportPluginsInput;
import org.hydev.mcpm.client.database.results.ExportPluginsResult;

/**
 * Defines how a user can export their plugins.
 *
 * @author Peter (https://github.com/MstrPikachu)
 * @since 2022-11-19
 */
public interface ExportPluginsBoundary {
    /**
     *
     * @param input The output stream to write to
     * @return Whether the export was successful or not
     */
    ExportPluginsResult export(ExportPluginsInput input);
}
