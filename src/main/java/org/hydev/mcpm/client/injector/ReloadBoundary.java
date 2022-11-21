package org.hydev.mcpm.client.injector;

/**
 * Interface for reload a locally installed plugin.
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-29
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
