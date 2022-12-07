package org.hydev.mcpm.client.uninstall;

import org.hydev.mcpm.client.injector.LocalJarBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.uninstall.FileRemove;

import java.io.File;

/**
 * Removes file for FileRemove.java
 */
public class RemoveFile implements FileRemove{
    private final LocalJarBoundary jarFinder;

    public RemoveFile(LocalJarBoundary jarFinder) {
        this.jarFinder = jarFinder;
    }

    public int removeFile(String pluginName) {
        // 2. If it isn't loaded, find the plugin jar file in local file system
        // (This will throw PluginNotFoundException when a plugin of the name in the file system
        // could not be found).
        File jar = null;
        try {
            jar = jarFinder.findJar(pluginName);
        } catch (PluginNotFoundException e) {
            return 1;
        }

        // 3. Delete plugin jar
        if (!jar.delete()) {
            return 2;
        }
        return 0;
    }
}
