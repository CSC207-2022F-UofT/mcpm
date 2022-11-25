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
import org.hydev.mcpm.SpigotEntry;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.utils.PluginJarFile;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import static org.hydev.mcpm.utils.ReflectionUtils.getPrivateField;
import static org.hydev.mcpm.utils.ReflectionUtils.setPrivateField;

/**
 * Implementation of plugin hot-loading/unloading
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class PluginLoader implements LoadBoundary, UnloadBoundary, ReloadBoundary
{
    private static final File HELPER_JAR = new File("plugins/mcpm-helper.jar");

    @Override
    public boolean loadPlugin(String name) throws PluginNotFoundException
    {
        // 1. Find plugin file by name
        var dir = new File("plugins");
        if (!dir.isDirectory()) throw new PluginNotFoundException(name);
        var file = Arrays.stream(Optional.ofNullable(dir.listFiles())
                .orElseThrow(() -> new PluginNotFoundException(name)))
            .filter(f -> f.getName().endsWith(".jar"))
            .filter(f -> {
                try (var jf = new PluginJarFile(f))
                {
                    return jf.readPluginYaml().name().equalsIgnoreCase(name);
                }
                catch (IOException | PluginYml.InvalidPluginMetaStructure ignored) { return false; }
            }).findFirst().orElseThrow(() -> new PluginNotFoundException(name));

        return loadPlugin(file);
    }

    @Override
    public boolean loadPlugin(File jar)
    {
        return loadPluginHelper(jar) != null;
    }

    /**
     * Helper for loadPlugin
     *
     * @param jar Jar file
     * @return Plugin instance or null
     */
    static @Nullable Plugin loadPluginHelper(File jar)
    {
        // 2. Load plugin
        var pm = Bukkit.getPluginManager();
        try
        {
            var plugin = pm.loadPlugin(jar);
            if (plugin == null) return null;

            // 3. Call onLoad()
            plugin.onLoad();
            pm.enablePlugin(plugin);
            return plugin;
        }
        catch (InvalidPluginException | InvalidDescriptionException e)
        {
            // These are errors indicating that the plugin we're trying to load is badly formatted.
            // There are nothing we can do.
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find a loaded plugin, throw error if not found
     *
     * @param name Plugin name (case-insensitive)
     * @return Plugin object
     * @throws PluginNotFoundException Not found
     */
    private Plugin findLoadedPlugin(String name) throws PluginNotFoundException
    {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins())
            .filter(p -> p.getName().equalsIgnoreCase(name)).findFirst()
            .orElseThrow(() -> new PluginNotFoundException(name));
    }

    @Override
    public void unloadPlugin(String name) throws PluginNotFoundException
    {
        // 1. Find plugin by name
        unloadPlugin(findLoadedPlugin(name));
    }

    /**
     * Unload plugin helper
     *
     * @param plugin Plugin object
     */
    static void unloadPlugin(Plugin plugin)
    {
        var pm = Bukkit.getPluginManager();

        // 2. Unload plugin
        pm.disablePlugin(plugin);

        // 3. Unregister listeners
        getPrivateField(pm, "listeners", new TypeToken<Map<Event, SortedSet<RegisteredListener>>>(){})
            .ifPresentOrElse(listeners -> listeners.values().forEach(set -> set.removeIf(l -> l.getPlugin() == plugin)),
                () -> System.err.println("listeners cannot be accessed"));

        // 4. Remove command from command map
        getPrivateField(pm, "commandMap", new TypeToken<SimpleCommandMap>(){}).ifPresentOrElse(cmdMap ->
            getPrivateField(cmdMap, "knownCommands", new TypeToken<Map<String, Command>>(){}).ifPresentOrElse(cmds ->
                cmds.values().stream()
                    .filter(cmd -> cmd instanceof PluginCommand).map(cmd -> (PluginCommand) cmd)
                    .filter(cmd -> cmd.getPlugin() == plugin).toList().forEach(cmd -> {
                        cmd.unregister(cmdMap);
                        cmds.values().removeIf(cmd::equals);
                    }), () -> System.err.println("knownCommands cannot be accessed")
            ), () -> System.err.println("commandMap cannot be accessed")
        );

        // 5. Remove plugin from list
        getPrivateField(pm, "plugins", new TypeToken<List<Plugin>>(){})
            .ifPresentOrElse(plugins -> plugins.remove(plugin),
                () -> System.err.println("plugins cannot be accessed"));

        // 6. Remove lookup name
        getPrivateField(pm, "lookupNames", new TypeToken<Map<String, Plugin>>(){})
            .ifPresentOrElse(names -> names.remove(plugin.getName()),
                () -> System.err.println("lookupNames cannot be accessed"));

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
    }

    @Override
    public void reloadPlugin(String name) throws PluginNotFoundException
    {
        if (name.equalsIgnoreCase("mcpm"))
        {
            reloadSelf();
            return;
        }
        unloadPlugin(name);
        loadPlugin(name);
    }

    /**
     * Attempt to reload this plugin
     */
    private void reloadSelf()
    {
        var pl = injectHelper();

        try
        {
            var jar = new File(PluginLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            // 4. Reflect! Since MCPM and MCPM-Helper are in different class loaders, I cannot call/cast it directly
            // or else it would give me a nonsense error "class PluginLoaderHelper cannot be cast to PluginLoaderHelper"
            var cls = pl.getClass();
            System.out.println(cls);
            System.out.println(Arrays.toString(cls.getDeclaredMethods()));
            var reload = cls.getDeclaredMethod("reloadMcpm", Plugin.class, File.class);
            reload.setAccessible(true);
            reload.invoke(pl, SpigotEntry.instance(), jar);

            // 5. (now this line ceased to exist). Check out unloadHelper() to continue
        }
        catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inject the helper jar needed for unloading myself
     *
     * @return Helper plugin instance
     */
    protected Plugin injectHelper()
    {
        try
        {
            // Return existing helper if it exists
            return findLoadedPlugin("MCPM-Helper");
        }
        catch (PluginNotFoundException ignored) { }

        try
        {
            // 1. Get the class bytecode from the jar
            var jar = new File(PluginLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            System.out.println(jar);
            try (var ji = new PluginJarFile(jar))
            {
                var yml = """
                    main: org.hydev.mcpm.client.injector.PluginLoaderHelper
                    name: MCPM-Helper
                    version: 1.0
                    api-version: 1.19
                    description: A temporary plugin that allows mcpm to reload itself
                    """.stripIndent();

                var classes = ji.list().stream().map(ZipEntry::getName)
                    .filter(it -> it.contains("org/hydev/mcpm/client/injector") ||
                        it.contains("org/hydev/mcpm/utils/ReflectionUtils") ||
                        it.contains("org/hydev/mcpm/client/models/PluginYml") ||
                        it.contains("org/hydev/mcpm/utils/PluginJarFile")).toList();

                // 2. Create a new jar file
                System.out.println("Creating jar file...");
                try (var jo = new JarOutputStream(new FileOutputStream(HELPER_JAR)))
                {
                    // Copy classes
                    for (String c : classes)
                    {
                        System.out.println("Copying " + c);
                        jo.putNextEntry(new JarEntry(c));
                        jo.write(ji.readBytes(c));
                        jo.closeEntry();
                    }

                    // Put yml
                    jo.putNextEntry(new JarEntry("plugin.yml"));
                    jo.write(yml.getBytes(StandardCharsets.UTF_8));
                    jo.closeEntry();
                }
            }

            // 3. Load the helper plugin
            return loadPluginHelper(HELPER_JAR);
        }
        catch (URISyntaxException | IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
