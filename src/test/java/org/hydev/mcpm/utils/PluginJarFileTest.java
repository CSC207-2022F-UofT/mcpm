package org.hydev.mcpm.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

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
    void readPluginYaml() throws IOException
    {
        var meta = jar.readPluginYaml();

        assert meta.main().equals("org.hydev.mc.ActiveList");
        assert meta.name().equals("ActiveList");
        assert meta.version().equals("1.1");
        assert meta.author().equals("Hykilpikonna");
    }
}
