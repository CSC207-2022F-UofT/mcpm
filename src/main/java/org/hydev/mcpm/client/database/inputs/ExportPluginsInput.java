package org.hydev.mcpm.client.database.inputs;

import java.io.OutputStream;

/**
 * Input for the ExportPluginsBoundary boundary.
 *
 * @param cache if true, will use local cache of plugins to export. Otherwise, will fetch plugins from the database.
 * @param out OutputStream to write to
 */
public record ExportPluginsInput(
        boolean cache,
        OutputStream out
) {
}
