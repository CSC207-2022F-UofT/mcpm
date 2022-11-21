package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginYml;

import java.io.File;
import java.util.List;

/**
 * Plugin tracker interface
 *
 * @author Kevin (https://github.com/kchprog)
 * @since 2022-09-27
 */
public interface PluginTracker {
    void addEntry(String name, boolean status);

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
