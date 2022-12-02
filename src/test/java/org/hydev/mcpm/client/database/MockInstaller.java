package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter;
import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.junit.jupiter.api.Assertions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MockInstaller implements InstallBoundary {
    private final List<PluginModel> plugins;
    private final PluginTracker tracker;
    private final boolean defaultResult;
    private final Set<String> requested = new HashSet<>();

    public MockInstaller(List<PluginModel> plugins, PluginTracker tracker) {
        this(plugins, tracker, true);
    }

    public MockInstaller(List<PluginModel> plugins, PluginTracker tracker, boolean defaultResult) {
        this.plugins = plugins;
        this.tracker = tracker;
        this.defaultResult = defaultResult;
    }

    @Override
    public boolean installPlugin(InstallInput installInput, InstallResultPresenter resultPresenter) {
        Assertions.assertEquals(installInput.type(), SearchPackagesType.BY_NAME);

        if (tracker.findIfInLockByName(installInput.name()))
            return false;

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

        return defaultResult;
    }

    Set<String> getRequested() {
        return new HashSet<>(requested);
    }
}
