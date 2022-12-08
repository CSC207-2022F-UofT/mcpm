package org.hydev.mcpm.client.export;

/**
 * Record storing information to export a plugin
 *
 * @param name Name of the plugin
 * @param version Version of the plugin
 */
public record ExportModel(
        String name,
        String version
) {
}
