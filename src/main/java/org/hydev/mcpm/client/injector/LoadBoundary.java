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
     */
    public void loadPlugin(String name) throws PluginNotFoundException;

    /**
     * Dynamically load a local plugin through JVM reflections and classloader hacks
     *
     * @param jar Local jar file path
     */
    public void loadPlugin(File jar);
}
