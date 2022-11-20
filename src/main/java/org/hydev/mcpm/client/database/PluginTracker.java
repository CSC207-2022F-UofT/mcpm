package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.models.PluginVersion;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.utils.HashUtils;
import org.hydev.mcpm.utils.PluginJarFile;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.*;  

interface PluginTracker {
    List<PluginYml> listInstalled();

    void setManuallyInstalled(String name);

    void removeManuallyInstalled(String name);

    List<String> listManuallyInstalled();

    List<String> listOrphanPlugins(boolean considerSoftDependencies);
    
    String getVersion(String name);
}