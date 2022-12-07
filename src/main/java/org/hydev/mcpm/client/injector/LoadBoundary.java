package org.hydev.mcpm.client.injector;

import java.io.File;

/**
 * Interface for loading a locally installed plugin.
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
    boolean loadPlugin(String name) throws PluginNotFoundException;
}
