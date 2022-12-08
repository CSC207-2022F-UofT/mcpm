package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.installer.output.InstallResult;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.junit.jupiter.api.Assertions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Mock installer utility that keeps track of plugins that were requested to be installed.
 */
public class MockInstaller implements InstallBoundary {
    private final List<PluginModel> plugins;
    private final PluginTracker tracker;
    private final InstallResult.Type defaultResult;
    private final Set<String> requested = new HashSet<>();

    /**
     * Creates a new MockInstaller object.
     *
     * @param plugins A list of plugins to lookup plugin related information about (to pass to PluginTracker).
     * @param tracker A plugin tracker to query whether a plugin is installed.
     * @param defaultResult Determines what the installer should return when installPlugin is invoked.
     */
    public MockInstaller(List<PluginModel> plugins, PluginTracker tracker, InstallResult.Type defaultResult) {
        this.plugins = plugins;
        this.tracker = tracker;
        this.defaultResult = defaultResult;
    }

    @Override
    public List<InstallResult> installPlugin(InstallInput installInput) {
        Assertions.assertEquals(installInput.type(), SearchPackagesType.BY_NAME);
        var name = installInput.name();

        if (tracker.findIfInLockByName(installInput.name()))
            return List.of(new InstallResult(InstallResult.Type.PLUGIN_EXISTS, name));

        var model = plugins.stream()
            .filter(plugin -> plugin.getLatestPluginVersion()
                .map(x -> x.meta() != null && installInput.name().equals(x.meta().name()))
                .orElse(false)
            ).findFirst();

        var modelId = model.map(PluginModel::id).orElse(0L);
        var versionId = model.map(x -> x.getLatestPluginVersion()
            .map(PluginVersion::id).orElse(0L)
        ).orElse(0L);

        requested.add(installInput.name());
        tracker.addEntry(installInput.name(), true, versionId, modelId);

        return List.of(new InstallResult(defaultResult, name));
    }

    /**
     * Returns a list of all plugin names that were passed to installPlugin (and succeeded).
     *
     * @return A list of plugin names.
     */
    Set<String> getRequested() {
        return new HashSet<>(requested);
    }
}
