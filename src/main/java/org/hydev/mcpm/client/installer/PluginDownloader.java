package org.hydev.mcpm.client.installer;

/**
  * Downloader for Spigot plugins
 */
public interface PluginDownloader {
    /**
     * Download a plugin
     *
     * @param pluginName   The name of the plugin.
     * @param pluginId      Spigot Plugin ID
     * @param pluginVersion Spigot Plugin Version ID
     */
    void download(String pluginName, long pluginId, long pluginVersion);
}

