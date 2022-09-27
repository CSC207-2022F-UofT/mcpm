package org.hydev.mcpm.client.injector;

import java.io.File;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class PluginLoader
{
    /**
     * Dynamically load a local plugin through JVM reflections and classloader hacks
     *
     * @param jar Local jar file path
     */
    public void loadPlugin(File jar)
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Dynamically unload a local plugin through JVM reflections and classloader hacks
     *
     * @param name Loaded plugin name
     */
    public void unloadPlugin(String name)
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Unload then load a plugin
     *
     * @param name Loaded plugin name
     */
    public void reloadPlugin(String name)
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }
}
