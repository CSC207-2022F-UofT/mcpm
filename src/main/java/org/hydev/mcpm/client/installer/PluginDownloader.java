package org.hydev.mcpm.client.installer;

public interface PluginDownloader {
    public void download (long pluginId, long pluginVersion, String destination);
}
