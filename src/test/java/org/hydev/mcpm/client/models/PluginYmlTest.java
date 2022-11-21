package org.hydev.mcpm.client.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

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

        assert meta.main().equals("org.hydev.mcpm.SpigotEntry");
        assert meta.name().equals("MCPM");
        assert meta.version().equals("1.1");
        assert meta.description().equals("Minecraft Package Manager for Bukkit/Spiget Servers");
        assert meta.apiVersion().equals("1.18");
        assert meta.author().equals("Hykilpikonna");
        assert meta.getFirstAuthor().equals("Hykilpikonna");
        assert meta.load() == null;
        assert meta.authors() == null;
        assert meta.website().equals("https://github.com/CSC207-2022F-UofT/mcpm");
        assert meta.commands().containsKey("mcpm");
        assert meta.commands().get("mcpm").description().equals("Minecraft package manager");
    }

    @Test
    void fromYmlFail()
    {

        String yml = """  
            main: org.hydev.mcpm.SpigotEntry
            name:[MCPM]
            version: 1.1
            """.stripIndent();

        try
        {
            var meta = PluginYml.fromYml(yml);
            assert false;
        }
        catch (PluginYml.InvalidPluginMetaStructure | JsonProcessingException ignored) { }


        String yml2 = """  
            [a, b, c]
            """.stripIndent();

        try
        {
            var meta = PluginYml.fromYml(yml2);
            assert false;
        }
        catch (PluginYml.InvalidPluginMetaStructure | JsonProcessingException ignored) { }
    }
}
