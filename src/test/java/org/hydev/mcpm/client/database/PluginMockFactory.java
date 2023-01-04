package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.models.PluginCommand;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * This class creates Plugins mainly for testing purposes.
 *
 * @author Taylor Whatley
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 */
public class PluginMockFactory {
    private PluginMockFactory() { }

    /**
     * Creates a mock PluginCommand object.
     *
     * @param description Description
     * @param aliases Other names
     * @return A Map[String, PluginCommand] object.
     */
    public static @NotNull Map<String, PluginCommand> createCommand(String description, List<String> aliases) {
        var map = new HashMap<String, PluginCommand>();
        var pc = new PluginCommand(description, aliases, null, null);
        for (String alias : aliases) {
            map.put(alias, pc);
        }
        return map;
    }

    /**
     * Creates a mock PluginYml object.
     *
     * @param name The name of the plugin.
     * @param version The version string for the plugin.
     * @param description The description for the plugin.
     * @param commands Commands for the plugin.
     * @return A PluginYml object.
     */
    public static PluginYml meta(String name, String version, String description,
                                 Map<String, PluginCommand> commands, List<String> authors) {
        return new PluginYml(
                "org." + name,
                name,
                version,
                description,
                null,
                null,
                null,
                authors != null ? new ArrayList<>(authors) : null, // optional chaining?
                null,
                null,
                null,
                null,
                null,
                null,
                commands
        );
    }

    /**
     * Creates a mock PluginYml object.
     *
     * @param name The name of the plugin.
     * @param version The version string for the plugin.
     * @param description The description for the plugin.
     * @return A PluginYml object.
     */
    public static PluginYml meta(String name, String version, String description) {
        return meta(name, version, description, null, null);
    }

    /**
     * Creates a mock PluginVersion object.
     *
     * @param id The version id.
     * @param name The plugin name (for meta).
     * @param string The version string (for meta).
     * @return A PluginVersion object.
     */
    public static PluginVersion version(long id, String name, String string) {
        return version(id, name, string, name, null);
    }

    /**
     * Creates a mock PluginVersion object.
     *
     * @param id The version id.
     * @param name The plugin name (for meta).
     * @param string The version string (for meta).
     * @param description The plugin description.
     * @param commands The commands in the plugin.
     * @return A PluginVersion object.
     */
    public static PluginVersion version(
        long id, String name, String string, String description,
        Map<String, PluginCommand> commands
    ) {
        return new PluginVersion(id, 0, "", meta(name, string, description, commands, null));
    }


    /**
     * Creates a mock PluginModel object (with no versions).
     *
     * @param id The plugin id.
     * @return A PluginModel object.
     */
    public static PluginModel model(long id) {
        return new PluginModel(id, 0, List.of());
    }

    /**
     * Creates a mock PluginModel object (with one version).
     * Thhe one version will have the version string `ver.{name}`
     *
     * @param id The plugin id.
     * @param name The plugin name.
     * @return A PluginModel object.
     */
    public static PluginModel model(long id, String name) {
        return new PluginModel(
                id, 0,
                List.of(version(id, name, "ver." + name)) // NOT SEMVER
        );
    }

    /**
     * Creates a mock PluginModel object.
     * The first version in versionNames will have id 0, the next will have id 1, and so on...
     * Commands are defaulted to null.
     *
     * @param id The plugin id.
     * @param name The plugin name.
     * @param versionNames The individual version strings for each version.
     * @return A PluginModel object.
     */
    public static PluginModel model(long id, String name, List<String> versionNames) {
        return model(id, name, name, versionNames, null);
    }

    /**
     * Creates a mock PluginModel object.
     * The first version in versionNames will have id 0, the next will have id 1, and so on...
     *
     * @param id The plugin id.
     * @param name The plugin name.
     * @param description The plugin description.
     * @param versionNames The individual version strings for each version.
     * @param commands Commands for the plugin.
     * @return A PluginModel object.
     */
    public static PluginModel model(long id, String name, String description, List<String> versionNames,
                                    Map<String, PluginCommand> commands) {
        return new PluginModel(
                id, 0,
                IntStream.range(0, versionNames.size())
                        .mapToObj(i -> version(i, name, versionNames.get(i), description, commands))
                        .toList()
        );
    }

    /**
     * Generates a sample list of plugins used for testing, containing the name, description, and commands.
     *
     * @return List of plugins for testing.
     */
    public static @Unmodifiable List<PluginModel> generateTestPlugins() {
        var worldGuardDescription = "WorldGuard lets you and players guard areas " +
            "of land against griefers and undesirables as well " +
            "as tweak and disable various gameplay features of Minecraft.";

        var multiverseDescription = "Multiverse was created at the dawn of Bukkit multiworld support. " +
            "It has since then grown into a complete world management " +
            "solution including special treatment of your nether worlds with " +
            "Multiverse NetherPortals.";

        var hologramDescription = "Create futuristic holograms to display text and items to players!";

        String[] descriptions = { worldGuardDescription, multiverseDescription, hologramDescription };

        return List.of(
                PluginMockFactory.model(1),
                PluginMockFactory.model(2, "WorldGuard",
                        descriptions[0],
                        List.of("1.18.2", "1.18.1"),
                        createCommand(descriptions[0], List.of("/god", "/ungod"))),
                PluginMockFactory.model(3, "Multiverse-Core", descriptions[1],
                        List.of("1.19.2", "1.19.1"),
                        createCommand(descriptions[1], List.of("/mvlist", "/mvl"))),
                PluginMockFactory.model(4, "Holographic Displays", descriptions[2],
                        List.of("1.19.2", "1.19.1", "1.19"),
                        createCommand(descriptions[2], List.of("/hd", "/ungod")))
        );
    }
}
