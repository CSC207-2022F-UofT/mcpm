package org.hydev.mcpm.client.injector;

import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.utils.PluginJarFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Implementation that finds locally installed jars by name
 *
 * @param dir Plugins directory (default to "./plugins")
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-27
 */
public record LocalJarFinder(File dir) implements LocalJarBoundary
{
    public LocalJarFinder()
    {
        this(new File("plugins"));
    }

    @Override
    public File findJar(String name) throws PluginNotFoundException
    {
        // 1. Find plugin file by name
        if (!dir.isDirectory()) throw new PluginNotFoundException(name);
        return Arrays.stream(Optional.ofNullable(dir.listFiles()).orElseThrow(() -> new PluginNotFoundException(name)))
            .filter(f -> f.getName().endsWith(".jar"))
            .filter(f -> {
                try (var jf = new PluginJarFile(f))
                {
                    return jf.readPluginYaml().name().equalsIgnoreCase(name);
                }
                catch (IOException | PluginYml.InvalidPluginMetaStructure ignored) { return false; }
            }).findFirst().orElseThrow(() -> new PluginNotFoundException(name));
    }
}
