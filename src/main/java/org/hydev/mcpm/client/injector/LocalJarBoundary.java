package org.hydev.mcpm.client.injector;

import java.io.File;

/**
 * Boundary for finding local jar files
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-27
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
