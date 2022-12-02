package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.models.PluginTrackerModel;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.utils.Pair;

import java.util.List;
import java.util.Map;

public record MockPluginTracker(
    Map<String, Boolean> entries,
    List<PluginYml> installed
) implements PluginTracker {
    private static Map<String, Boolean> makeEntries(List<PluginYml> installed) {
        return installed.stream()
            .map(x -> Pair.of(x.name(), true))
            .collect(Pair.<String, Boolean>toMap());
    }

    public MockPluginTracker(List<PluginYml> installed) {
        this(makeEntries(installed), installed);
    }


    @Override
    public void addEntry(String name, boolean status, long versionId, long pluginId) {
        entries.put(name, status);
    }

    @Override
    public void removeEntry(String name) {
        entries.remove(name);
    }

    @Override
    public List<PluginTrackerModel> listEntries() {
        return null;
    }

    @Override
    public List<PluginYml> listInstalled() {
        return installed;
    }

    @Override
    public void setManuallyInstalled(String name) {
        if (!entries.containsKey(name)) {
            return;
        }

        entries.put(name, true);
    }

    @Override
    public void removeManuallyInstalled(String name) {
        if (!entries.containsKey(name)) {
            return;
        }

        entries.put(name, false);
    }

    @Override
    public Boolean findIfInLockById(String id) {
        return null;
    }

    @Override
    public List<String> listManuallyInstalled() {
        return entries().entrySet().stream()
            .filter(Map.Entry::getValue)
            .map(Map.Entry::getKey)
            .toList();
    }

    @Override
    public List<PluginYml> listOrphanPlugins(boolean considerSoftDependencies) {
        return List.of();
    }

    @Override
    public Boolean findIfInLockByName(String name) {
        return entries.containsKey(name);
    }
}
