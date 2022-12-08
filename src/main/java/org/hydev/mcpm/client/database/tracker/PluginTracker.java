package org.hydev.mcpm.client.database.tracker;

import org.hydev.mcpm.client.models.PluginTrackerModel;
import org.hydev.mcpm.client.models.PluginYml;

import java.util.List;

/**
 * Extended plugin tracker interface (with methods for specific
 * versionId/pluginId).
 */
public interface PluginTracker
{
    void addEntry(String name, boolean status, long versionId, long pluginId);

    void removeEntry(String name);

    List<PluginTrackerModel> listEntries();

    List<PluginYml> listInstalled();

    Boolean findIfInLockByName(String name);

    List<String> listManuallyInstalled();

    List<PluginYml> listOrphanPlugins(boolean considerSoftDependencies);
}
