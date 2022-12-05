package org.hydev.mcpm.client.models;

/**
 * Representation of a plugin for use in the local plugin tracker.
 */
public class PluginTrackerModel {
    private String name;
    private boolean isManual;
    private Long versionId;
    private Long pluginId;

    public PluginTrackerModel()
    {
    }

    /**
     * Constructor for PluginTrackerModel.
     *
     * @param name      The name of the plugin.
     * @param isManual  Whether the plugin is manually installed.
     * @param versionId The version ID of the plugin.
     * @param pluginId  The plugin ID of the plugin.
     */
    public PluginTrackerModel(String name, boolean isManual, Long versionId, Long pluginId) {
        this.name = name;
        this.isManual = isManual;
        this.versionId = versionId;
        this.pluginId = pluginId;
    }

    public String getName() {
        return name;
    }

    public Boolean isManual() {
        return isManual;
    }

    public Long getVersionId() {
        return versionId;
    }

    public Long getPluginId() {
        return pluginId;
    }

    public void setManual(boolean newBool) {
        this.isManual = newBool;
    }
}
