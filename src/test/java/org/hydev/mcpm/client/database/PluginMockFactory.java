package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.fetcher.BriefFetcherListener;
import org.hydev.mcpm.client.database.fetcher.ConstantFetcher;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Mock factory for testing search functionality
 */
public class PluginMockFactory {
    private PluginMockFactory() { }

    /**
     * Creates a mock PluginYml object.
     *
     * @param name The name of the plugin.
     * @param version The version string for the plugin.
     * @param description The description for the plugin.
     * @return A PluginYml object.
     */
    public static PluginYml meta(String name, String version, String description) {
        return new PluginYml(
            "org." + name,
            name,
            version,
            description,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
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
        return new PluginVersion(id, 0, "", meta(name, string, null));
    }


    /**
     * Creates a mock PluginModel object (with no versions).
     *
     * @param id The plugin id.
     * @return A PluginModel object.
     */
    public static PluginModel model(long id) {
        return new PluginModel(id, List.of());
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
            id,
            List.of(version(id, name, "ver." + name)) // NOT SEMVER
        );
    }

    /**
     * Creates a mock PluginModel object.
     * The first version in versionNames will have id 0, the next will have id 1, and so on...
     *
     * @param id The plugin id.
     * @param name The plugin name.
     * @param versionNames The individual version strings for each version.
     * @return A PluginModel object.
     */
    public static PluginModel model(long id, String name, List<String> versionNames) {
        return new PluginModel(
            id,
            IntStream.range(0, versionNames.size())
                .mapToObj(i -> version(i, name, versionNames.get(i)))
                .toList()
        );
    }

    /**
     * Creates a mock DatabaseInteractor object with the provided plugin list.
     *
     * @param plugins A list of plugins that the DatabaseInteractor will have access to.
     * @return A DatabaseInteractor object.
     */
    public static DatabaseInteractor interactor(List<PluginModel> plugins) {
        var fetcher = new ConstantFetcher(plugins);
        var listener = new BriefFetcherListener(true);

        return new DatabaseInteractor(fetcher, listener);
    }

    public static List<PluginModel> generatePlugins() {
        List<PluginModel> plugins = new ArrayList<>();
        String[] names = {"WorldGuard"};
        String [] descriptions = {"Protect your server!\n" +
                "WorldGuard lets you and players guard areas of land against griefers and undesirables\n" +
                "as well as tweak and disable various gameplay features of Minecraft."};
        return plugins;
    }
}
