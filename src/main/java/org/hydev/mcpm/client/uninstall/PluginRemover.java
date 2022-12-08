package org.hydev.mcpm.client.uninstall;

import org.hydev.mcpm.client.loader.LocalJarBoundary;
import org.hydev.mcpm.client.loader.PluginNotFoundException;

import java.io.File;

/**
 * Removes file for FileRemove.java
 */
public class PluginRemover implements FileRemove {
    private final LocalJarBoundary jarFinder;

    public PluginRemover(LocalJarBoundary jarFinder) {
        this.jarFinder = jarFinder;
    }

    @Override
    public FileRemoveResult removeFile(String pluginName) {
        // 2. If it isn't loaded, find the plugin jar file in local file system
        // (This will throw PluginNotFoundException when a plugin of the name in the file system
        // could not be found).
        File jar = null;
        try {
            jar = jarFinder.findJar(pluginName);
        } catch (PluginNotFoundException e) {
            return FileRemoveResult.NOT_FOUND;
        }

        // 3. Delete plugin jar
        if (!jar.delete()) {
            return FileRemoveResult.FAILED_TO_DELETE;
        }
        return FileRemoveResult.SUCCESS;
    }
}
