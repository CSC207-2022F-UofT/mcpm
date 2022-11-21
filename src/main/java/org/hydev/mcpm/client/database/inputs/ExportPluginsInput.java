package org.hydev.mcpm.client.database.inputs;

import java.io.OutputStream;

/**
 * Input for the ExportPluginsBoundary boundary.
 *
 * @param cache if true, will use local cache of plugins to export. Otherwise, will fetch plugins from the database.
 * @param out OutputStream to write to
 * @author Peter (https://github.com/MstrPikachu)
 * @since 2022-11-19
 */
public record ExportPluginsInput(
        boolean cache,
        OutputStream out
) {
}
