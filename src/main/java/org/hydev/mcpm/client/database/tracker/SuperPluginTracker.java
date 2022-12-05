package org.hydev.mcpm.client.database.tracker;

import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.client.models.PluginTrackerModel;
import org.hydev.mcpm.client.search.SearchPackagesBoundary;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * Extended plugin tracker interface (with methods for specific
 * versionId/pluginId).
 */
public interface SuperPluginTracker {
    void addEntry(String name, boolean status, String versionId, String pluginId);

    void removeEntry(String name);

    List<PluginTrackerModel> listEntries();

    List<PluginYml> listInstalled();

    ArrayList<PluginTrackerModel> listInstalledAsModels();

    void setManuallyInstalled(String name);

    void removeManuallyInstalled(String name);

    Boolean findIfInLockById(String id);

    Boolean findIfInLockByName(String name);

    List<String> listManuallyInstalled();

    List<String> listOrphanPlugins(boolean considerSoftDependencies);

    String getVersion(String name);

    List<PluginYml> listOutdatedPluginYml(SearchPackagesBoundary searchPackagesBoundary);

    Boolean compareVersion(String name, SearchPackagesBoundary searchPackagesBoundary);

    Boolean compareVersionNew(File local, PluginModel remote);
}
