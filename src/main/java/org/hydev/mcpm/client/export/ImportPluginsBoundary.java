package org.hydev.mcpm.client.export;

/**
 * Defines how a user can export their plugins.
 *
 * @author Peter (https://github.com/MstrPikachu)
 * @since 2022-11-19
 */
public interface ImportPluginsBoundary {
    /**
     * Install and load the plugins
     *
     * @param input The plugins to import
     * @return Whether the export was successful or not
     */
    ImportResult importPlugins(String input) throws ImportException;
}
