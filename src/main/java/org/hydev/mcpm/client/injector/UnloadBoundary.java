package org.hydev.mcpm.client.injector;

/**
 * Interface for unloading a locally installed plugin.
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-29
 */
public interface UnloadBoundary
{
    /**
     * Dynamically unload a local plugin through JVM reflections and classloader hacks
     *
     * @param name Loaded plugin name
     */
    public void unloadPlugin(String name) throws PluginNotFoundException;
}
