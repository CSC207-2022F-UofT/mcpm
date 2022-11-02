package org.hydev.mcpm.client.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

import static org.hydev.mcpm.Constants.YML;

/**
 * POJO model for plugin.yml inside each Minecraft Bukkit/Spigot plugin.
 * <p>
 * For specifications of each field; please visit https://www.spigotmc.org/wiki/plugin-yml/
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-02
 */
public record PluginYml(
    @NotNull String main,
    @NotNull String name,
    @NotNull String version,
    String description,
    String apiVersion,
    String load,
    String author,
    ArrayList<String> authors,
    String website,
    ArrayList<String> depend,
    String prefix,
    ArrayList<String> softdepend,
    ArrayList<String> loadbefore,
    ArrayList<String> libraries,
    Map<String, Object> commands,
    Map<String, Object> permissions
)
{
    /**
     * Parse plugin.yml from yml string
     *
     * @param yml YML string
     * @return PluginYml object
     */
    public static PluginYml fromYml(String yml) throws JsonProcessingException
    {
        yml = yml.replaceAll("api-version", "apiVersion");

        // Try to fix mistakes that plugin authors might make
        var parsed = (ObjectNode) YML.readTree(yml);

        // Common mistake: Listing multiple authors under "author" while they should be under "authors"
        if (parsed.get("author").isArray())
        {
            parsed.set("authors", parsed.get("authors"));
            parsed.remove("authors");
        }

        return YML.treeToValue(parsed, PluginYml.class);
    }
}
