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
import org.hydev.mcpm.utils.PluginJarFile;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.*;

import static org.hydev.mcpm.utils.ReflectionUtils.getPrivateField;
import static org.hydev.mcpm.utils.ReflectionUtils.setPrivateField;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class PluginLoader implements LoadBoundary, UnloadBoundary, ReloadBoundary
{
    @Override
    public void loadPlugin(String name) throws PluginNotFoundException
    {
        // 1. Find plugin file by name
        var nf = new PluginNotFoundException(name);
        var dir = new File("plugins");
        if (!dir.isDirectory()) throw nf;
        var file = Arrays.stream(Optional.ofNullable(dir.listFiles()).orElseThrow(() -> nf))
            .filter(f -> f.getName().endsWith(".jar"))
            .filter(f -> {
                try (var jf = new PluginJarFile(f))
                {
                    return jf.readPluginYaml().getName().equalsIgnoreCase(name);
                }
                catch (IOException ignored) { return false; }
            }).findFirst().orElseThrow(() -> nf);

        loadPlugin(file);
    }

    @Override
    public void loadPlugin(File jar)
    {
        // 2. Load plugin
        var pm = Bukkit.getPluginManager();
        try
        {
            var plugin = pm.loadPlugin(jar);
            assert plugin != null;

            // 3. Call onLoad()
            plugin.onLoad();
            pm.enablePlugin(plugin);
        }
        catch (InvalidPluginException | InvalidDescriptionException e)
        {
            // These are errors indicating that the plugin we're trying to load is badly formatted.
            // There are nothing we can do.
            e.printStackTrace();
        }
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
