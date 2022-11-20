package org.hydev.mcpm.utils;

import org.hydev.mcpm.client.models.PluginYml;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Tests for org.hydev.mcpm.utils.PluginJarFile
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-02
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
        assert jar.readString("plugin.yml").split("\n")[0].strip().equals("name: ActiveList");
    }

    @Test
    void readPluginYaml() throws IOException, PluginYml.InvalidPluginMetaStructure
    {
        var meta = jar.readPluginYaml();

        assert meta.main().equals("org.hydev.mc.ActiveList");
        assert meta.name().equals("ActiveList");
        assert meta.version().equals("1.1");
        assert meta.author().equals("Hykilpikonna");
        assert meta.commands().containsKey("activelist");
        assert meta.commands().get("activelist").aliases().equals(List.of("al", "ll"));

        // Obtain all command names and aliases of a plugin meta
        var cmds = Stream.concat(meta.commands().values().stream().flatMap(c -> c.aliases().stream()),
            meta.commands().keySet().stream()).toList();
        System.out.println(cmds);
    }
}
