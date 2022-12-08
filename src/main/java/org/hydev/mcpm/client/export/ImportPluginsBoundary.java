package org.hydev.mcpm.client.export;

/**
 * Defines how a user can export their plugins.
 */
public interface ImportPluginsBoundary {
    /**
     * Install and load the plugins
     *
     * @param input Specification of where to import from
     * @return Whether the export was successful or not
     */
    ImportResult importPlugins(ImportInput input);
}
