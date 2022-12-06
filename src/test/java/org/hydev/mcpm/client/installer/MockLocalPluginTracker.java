package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.database.PluginMockFactory;
import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.models.PluginTrackerModel;
import org.hydev.mcpm.client.models.PluginYml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockLocalPluginTracker implements PluginTracker {
    private Map<String, Boolean> localJarFilesTracker;
    private List<PluginYml> pluginInstalled;

    public MockLocalPluginTracker() {
        this.localJarFilesTracker = new HashMap<>();
        this.pluginInstalled = new ArrayList<>();
    }

    @Override
    public void addEntry(String name, boolean status, long versionId, long pluginId) {
        localJarFilesTracker.put(name, false);
        pluginInstalled.add(PluginMockFactory.meta(name, String.valueOf(versionId), ""));
    }

    @Override
    public void removeEntry(String name) {
        localJarFilesTracker.remove(name);
        for (PluginYml pluginYml:pluginInstalled) {
            if (pluginYml.name() == name) {
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
    public void setManuallyInstalled(String name) {
        if (!localJarFilesTracker.containsKey(name)) {
            return;
        }
        localJarFilesTracker.put(name, true);
    }

    @Override
    public void removeManuallyInstalled(String name) {
        if (!localJarFilesTracker.containsKey(name)) {
            return;
        }
        localJarFilesTracker.put(name, false);
    }

    @Override
    public Boolean findIfInLockById(String id) {
        return null;
    }

    @Override
    public Boolean findIfInLockByName(String name) {
        if (localJarFilesTracker.containsKey(name)) {
            return true;
        }
        return false;
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
