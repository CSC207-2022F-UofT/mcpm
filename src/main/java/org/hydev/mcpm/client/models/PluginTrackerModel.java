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

    /**
     * Constructor for PluginTrackerModel.
     *
     * @param name      The name of the plugin.
     * @param isManual  Whether the plugin is manually installed.
     * @param versionId The version ID of the plugin.
     * @param pluginId  The plugin ID of the plugin.
     */
    public PluginTrackerModel(String name, Boolean isManual, String versionId, String pluginId) {
        this.name = name;
        this.isManual = isManual;
        this.versionId = versionId;
        this.pluginId = pluginId;
    }

    /**
     * Constructor for PluginTrackerModel.
     * Instantiates a PluginTrackerModel object from a string.
     */
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

    public Boolean isManual() {
        return isManual;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setManual(boolean newBool) {
        this.isManual = newBool;
    }
}