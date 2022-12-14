package org.hydev.mcpm.utils;

import org.hydev.mcpm.client.models.PluginYml;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for org.hydev.mcpm.utils.PluginJarFile
 */
class PluginJarFileTest
{
    PluginJarFile jar;

    @BeforeEach
    void setUp() throws IOException
    {
        // Open our test plugin
        jar = new PluginJarFile(Objects.requireNonNull(GeneralUtils.getResourceFile("test-plugin-activelist.jar")));
    }

    @Test
    void readString() throws IOException
    {
        // Test reading the first line of plugin.yml
        assertEquals(jar.readString("plugin.yml").split("\n")[0].strip(), "name: ActiveList");
    }

    @Test
    void readPluginYaml() throws IOException, PluginYml.InvalidPluginMetaStructure
    {
        var meta = jar.readPluginYaml();

        assertEquals(meta.main(), "org.hydev.mc.ActiveList");
        assertEquals(meta.name(), "ActiveList");
        assertEquals(meta.version(), "1.1");
        assertEquals(meta.getFirstAuthor(), "Hykilpikonna");
        assertTrue(meta.commands().containsKey("activelist"));
        assertEquals(meta.commands().get("activelist").aliases(), List.of("al", "ll"));

        // Obtain all command names and aliases of a plugin meta
        var cmds = Stream.concat(meta.commands().values().stream().flatMap(c -> c.aliases().stream()),
            meta.commands().keySet().stream()).toList();
        System.out.println(cmds);
    }
}
