package org.hydev.mcpm.client.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.JacksonYAMLParseException;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.error.MarkedYAMLException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PluginYmlTest
{
    @Test
    void fromYml() throws PluginYml.InvalidPluginMetaStructure, JsonProcessingException
    {
        String yml = """  
            main: org.hydev.mcpm.SpigotEntry
            name: MCPM
            version: 1.1
            description: Minecraft Package Manager for Bukkit/Spiget Servers
            api-version: 1.18
            authors: Hykilpikonna
            website: https://github.com/CSC207-2022F-UofT/mcpm
            commands:
              mcpm:
                description: Minecraft package manager
            """.stripIndent();

        var meta = PluginYml.fromYml(yml);

        assertEquals(meta.main(), "org.hydev.mcpm.SpigotEntry");
        assertEquals(meta.name(), "MCPM");
        assertEquals(meta.version(), "1.1");
        assertEquals(meta.description(), "Minecraft Package Manager for Bukkit/Spiget Servers");
        assertEquals(meta.apiVersion(), "1.18");
        assertEquals(meta.author(), "Hykilpikonna");
        assertEquals(meta.getFirstAuthor(), "Hykilpikonna");
        assert meta.load() == null;
        assert meta.authors() == null;
        assertEquals(meta.website(), "https://github.com/CSC207-2022F-UofT/mcpm");
        assert meta.commands().containsKey("mcpm");
        assertEquals(meta.commands().get("mcpm").description(), "Minecraft package manager");
    }

    @Test
    void fromYmlFail()
    {
        String yml = """  
            main: org.hydev.mcpm.SpigotEntry
            name:[MCPM]
            version: 1.1
            """.stripIndent();

        assertThrows(JacksonYAMLParseException.class, () -> PluginYml.fromYml(yml));
    }

    @Test
    void fromYmlFail2()
    {
        String yml = """  
            [a, b, c]
            """.stripIndent();

        assertThrows(PluginYml.InvalidPluginMetaStructure.class, () -> PluginYml.fromYml(yml));
    }
}
