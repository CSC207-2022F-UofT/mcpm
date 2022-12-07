package org.hydev.mcpm.client.loader;

/**
 * Interface for reload a locally installed plugin.
 */
public interface ReloadBoundary
{
    /**
     * Unload then load a plugin
     *
     * @param name Loaded plugin name
     */
    void reloadPlugin(String name) throws PluginNotFoundException;
}
