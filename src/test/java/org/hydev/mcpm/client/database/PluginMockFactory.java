package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.fetcher.BriefFetcherListener;
import org.hydev.mcpm.client.database.fetcher.ConstantFetcher;
import org.hydev.mcpm.client.database.model.PluginVersionId;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;

import java.util.List;
import java.util.stream.IntStream;

public class PluginMockFactory {
    private PluginMockFactory() { }

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

    public static PluginVersion version(long id, String name, String string) {
        return new PluginVersion(id, 0, "", meta(name, string, null));
    }


    public static PluginModel model(long id) {
        return new PluginModel(id, List.of());
    }

    public static PluginModel model(long id, String name) {
        return new PluginModel(
            id,
            List.of(version(id, name, "ver." + name)) // NOT SEMVER
        );
    }

    public static PluginModel model(long id, String name, List<String> versionNames) {
        return new PluginModel(
            id,
            IntStream.range(0, versionNames.size())
                .mapToObj(i -> version(i, name, versionNames.get(i)))
                .toList()
        );
    }

    public static DatabaseInteractor interactor(List<PluginModel> plugins) {
        var fetcher = new ConstantFetcher(plugins);
        var listener = new BriefFetcherListener(true);

        return new DatabaseInteractor(fetcher, listener);
    }
}
