package org.hydev.mcpm.client.injector;

import java.io.File;

/**
 * Interface for loading a locally installed plugin.
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-29
 */
public interface LoadBoundary
{
    /**
     * Dynamically load a local plugin through JVM reflections and classloader hacks
     *
     * @param name Loaded plugin name
     * @return True if success, false if failed
     * @throws PluginNotFoundException Plugin of the name is not found in the plugins directory
     */
    public boolean loadPlugin(String name) throws PluginNotFoundException;

    /**
     * Dynamically load a local plugin through JVM reflections and classloader hacks
     *
     * @param jar Local jar file path
     * @return True if success, false if failed
     */
    public boolean loadPlugin(File jar);
}
