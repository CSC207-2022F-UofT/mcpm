package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.client.search.SearchPackagesBoundary;

import java.io.File;
import java.util.List;

/**
 * Plugin tracker interface
 */
public interface SuperPluginTracker {
    void addEntry(String name, boolean status, String versionId, String pluginId);

    void removeEntry(String name);

    List<PluginYml> listInstalled();

    void setManuallyInstalled(String name);

    void removeManuallyInstalled(String name);

    List<String> listManuallyInstalled();

    List<String> listOrphanPlugins(boolean considerSoftDependencies);

    String getVersion(String name);

    List<PluginYml> listOutdatedPluginYml(SearchPackagesBoundary searchPackagesBoundary);

    Boolean compareVersion(String name, SearchPackagesBoundary searchPackagesBoundary);

    Boolean compareVersionNew(File local, PluginModel remote);
}
