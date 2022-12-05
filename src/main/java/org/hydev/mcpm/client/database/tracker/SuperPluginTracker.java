package org.hydev.mcpm.client.database.tracker;

import org.hydev.mcpm.client.models.Database;
import org.hydev.mcpm.client.models.PluginTrackerModel;
import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;

import java.util.List;
import java.util.ArrayList;

/**
 * Extended plugin tracker interface (with methods for specific
 * versionId/pluginId).
 */
public interface SuperPluginTracker {
    void addEntry(String name, boolean status, long versionId, long pluginId);

    void removeEntry(String name);

    List<PluginTrackerModel> listEntries();

    List<PluginYml> listInstalled();

    ArrayList<PluginTrackerModel> listInstalledAsModels();

    void setManuallyInstalled(String name);

    void removeManuallyInstalled(String name);

    Boolean findIfInLockById(String id);

    Boolean findIfInLockByName(String name);

    List<String> listManuallyInstalled();

    List<PluginYml> listOrphanPlugins(boolean considerSoftDependencies);

    List<PluginVersion> listOutdatedPlugins(Database database);
}
