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
    Map<String, Object> commands
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

        // The YAML parser doesn't like \t tab characters
        yml = yml.replace("\t", "    ");

        // Some yml files are full of NUL characters, IDK why
        yml = yml.replace("\0", "");

        // Some people forget to put : after their version number
        yml = yml.replaceAll("\nversion +(?=\\d)", "\nversion: ");

        // Try to fix mistakes that plugin authors might make
        var parsed = (ObjectNode) YML.readTree(yml);

        // Common mistake: Listing multiple authors under "author" while they should be under "authors"
        if (parsed.has("author") && parsed.get("author").isArray())
            parsed.set("authors", parsed.remove("author"));

        // Same goes for the reverse, some developers put a single name under "authors"
        if (parsed.has("authors") && !parsed.get("authors").isArray() && parsed.get("authors").isTextual())
            parsed.set("author", parsed.remove("authors"));

        // Some people put [PLUGIN] as their prefix without realizing that it will be parsed as an array
        if (parsed.has("prefix") && parsed.get("prefix").isArray())
            parsed.set("prefix", parsed.get("prefix").get(0));

        return YML.treeToValue(parsed, PluginYml.class);
    }
}
