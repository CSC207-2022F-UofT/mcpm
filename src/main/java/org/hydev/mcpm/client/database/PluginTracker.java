package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.utils.HashUtils;
import org.hydev.mcpm.utils.PluginJarFile;
import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.models.PluginModel;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

interface PluginTracker {
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
