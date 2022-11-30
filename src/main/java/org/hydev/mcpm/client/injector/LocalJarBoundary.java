package org.hydev.mcpm.client.injector;

import java.io.File;

/**
 * Boundary for finding local jar files
 */
public interface LocalJarBoundary
{
    /**
     * Find a jar file of a plugin in file system by name
     *
     * @param name Plugin name in meta
     * @return Plugin jar file
     */
    File findJar(String name) throws PluginNotFoundException;
}
