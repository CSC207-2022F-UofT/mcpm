package org.hydev.mcpm.client.injector;

import com.google.common.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import static org.hydev.mcpm.utils.ReflectionUtils.getPrivateField;
import static org.hydev.mcpm.utils.ReflectionUtils.setPrivateField;

/**
 * Implementation of plugin hot-loading/unloading
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public record PluginLoader(LocalJarFinder jarFinder) implements LoadBoundary, UnloadBoundary, ReloadBoundary
{
    @Override
    public boolean loadPlugin(String name) throws PluginNotFoundException
    {
        return loadPlugin(jarFinder.findJar(name));
    }

    @Override
    public boolean loadPlugin(File jar)
    {
        // 2. Load plugin
        var pm = Bukkit.getPluginManager();
        try
        {
            var plugin = pm.loadPlugin(jar);
            if (plugin == null) return false;

            // 3. Call onLoad()
            plugin.onLoad();
            pm.enablePlugin(plugin);
            return true;
        }
        catch (InvalidPluginException | InvalidDescriptionException e)
        {
            // These are errors indicating that the plugin we're trying to load is badly formatted.
            // There are nothing we can do.
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public File unloadPlugin(String name) throws PluginNotFoundException
    {
        var pm = Bukkit.getPluginManager();

        // 1. Find plugin by name
        var plugin = Arrays.stream(pm.getPlugins()).filter(p -> p.getName().equalsIgnoreCase(name)).findFirst()
            .orElseThrow(() -> new PluginNotFoundException(name));
        var jar = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        // 2. Unload plugin
        pm.disablePlugin(plugin);

        // 3. Unregister listeners
        getPrivateField(pm, "listeners", new TypeToken<Map<Event, SortedSet<RegisteredListener>>>(){})
            .ifPresent(listeners -> listeners.values().forEach(set -> set.removeIf(l -> l.getPlugin() == plugin)));

        // 4. Remove command from command map
        getPrivateField(pm, "commandMap", new TypeToken<SimpleCommandMap>(){}).ifPresent(cmdMap ->
            getPrivateField(pm, "knownCommands", new TypeToken<Map<String, Command>>(){}).ifPresent(cmds ->
                cmds.values().stream()
                    .filter(cmd -> cmd instanceof PluginCommand).map(cmd -> (PluginCommand) cmd)
                    .filter(cmd -> cmd.getPlugin() == plugin).toList().forEach(cmd -> {
                        cmd.unregister(cmdMap);
                        cmds.values().removeIf(cmd::equals);
                    })
            )
        );

        // 5. Remove plugin from list
        getPrivateField(pm, "plugins", new TypeToken<List<Plugin>>(){})
            .ifPresent(plugins -> plugins.remove(plugin));

        // 6. Remove lookup name
        getPrivateField(pm, "lookupNames", new TypeToken<Map<String, Plugin>>(){})
            .ifPresent(names -> names.remove(plugin.getName()));

        // 7. Unload Java classes so that the jar can be deleted on Windows
        if (plugin.getClass().getClassLoader() instanceof URLClassLoader cl)
        {
            if (!setPrivateField(cl, "plugin", null) || !setPrivateField(cl, "pluginInit", null))
                System.err.println("Error in setPrivateField, skipping unload");

            // Close classloader
            try
            {
                cl.close();
                System.gc();
            }
            catch (IOException e)
            {
                // File IO error on the hard drive, there are nothing the program can do.
                e.printStackTrace();
            }
        }

        return jar;
    }

    @Override
    public void reloadPlugin(String name) throws PluginNotFoundException
    {
        unloadPlugin(name);
        loadPlugin(name);
    }
}
