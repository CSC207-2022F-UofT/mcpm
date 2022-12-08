package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.database.PluginMockFactory;
import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.models.PluginTrackerModel;
import org.hydev.mcpm.client.models.PluginYml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock implementation of the PluginTracker interface.
 *
 */
public class MockLocalPluginTracker implements PluginTracker {
    private final Map<String, Boolean> localJarFilesTracker;
    private final List<PluginYml> pluginInstalled;

    public MockLocalPluginTracker() {
        this.localJarFilesTracker = new HashMap<>();
        this.pluginInstalled = new ArrayList<>();
    }

    /**
     * status: true - manual, false - auto
     *
     */
    @Override
    public void addEntry(String name, boolean status, long versionId, long pluginId) {
        localJarFilesTracker.put(name, false);
        pluginInstalled.add(PluginMockFactory.meta(name, String.valueOf(versionId), ""));
    }

    @Override
    public void removeEntry(String name) {
        localJarFilesTracker.remove(name);
        for (PluginYml pluginYml : pluginInstalled) {
            if (pluginYml.name().equals(name)) {
                listInstalled().remove(pluginYml);
            }
        }
    }

    @Override
    public List<PluginTrackerModel> listEntries() {
        return null;
    }

    @Override
    public List<PluginYml> listInstalled() {
        return pluginInstalled;
    }

    @Override
    public Boolean findIfInLockByName(String name) {
        return localJarFilesTracker.containsKey(name);
    }

    @Override
    public List<String> listManuallyInstalled() {
        return localJarFilesTracker.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public List<PluginYml> listOrphanPlugins(boolean considerSoftDependencies) {
        return null;
    }
}
