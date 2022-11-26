package org.hydev.mcpm.client.uninstall;

import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.injector.PluginNotFoundException;

import java.io.File;
import java.util.Arrays;


/**
 * Uninstall use case interactor
 *
 * @author Anushka (https://github.com/aanushkasharma)
 */
public class Uninstaller implements UninstallBoundary {
    @Override
    public void uninstall(String name) throws PluginNotFoundException {
        PluginLoader pil = new PluginLoader();
        pil.unloadPlugin(name);

        // Find plugin file
        // could be useful for removing dependencies (?)
        var pluginFolder = new File("plugins");
        var files = pluginFolder.listFiles();
        if (files == null) {
            files = new File[]{};
        }

        var plugins = Arrays.stream(files)
                .filter(plugin -> plugin.getName().endsWith(name + ".jar")).toList();

        if (plugins.size() == 0) {
            throw new PluginNotFoundException("Plugin " + name + " not found.");
        }

        File file = new File(plugins.get(0).toString());

        // Delete plugin
        if (file.delete()) {
            System.out.println("File deleted successfully");
        } else {
            System.out.println("Failed to delete the file");
        }
    }
}
