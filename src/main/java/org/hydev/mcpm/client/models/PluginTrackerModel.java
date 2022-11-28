package org.hydev.mcpm.client.models;

/**
 * 
 * Representation of a plugin for use in the local plugin tracker.
 */

public class PluginTrackerModel {
    String name;
    Boolean isManual;
    String versionId;
    String pluginId;

    public PluginTrackerModel(String name, Boolean isManual, String versionId, String pluginId) {
        this.name = name;
        this.isManual = isManual;
        this.versionId = versionId;
        this.pluginId = pluginId;
    }

    public PluginTrackerModel(String stringRepresentation) {
        String[] split = stringRepresentation.split(",");
        this.name = split[0];
        this.isManual = Boolean.parseBoolean(split[1]);
        this.versionId = split[2];
        this.pluginId = split[3];
    }

    public String getName() {
        return name;
    }

    public Boolean getManual() {
        return isManual;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getPluginId() {
        return pluginId;
    }
}