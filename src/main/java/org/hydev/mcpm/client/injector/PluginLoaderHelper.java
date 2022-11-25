package org.hydev.mcpm.client.injector;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Entry point for plugin helper in order to reload itself
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-24
 */
public class PluginLoaderHelper extends JavaPlugin
{
    public void reloadMcpm(Plugin instance, File jar)
    {
        System.out.println("Unloading MCPM...");
        PluginLoader.unloadPlugin(instance);
        System.out.println("Loading MCPM...");
        PluginLoader.loadPluginHelper(jar);
    }
}
