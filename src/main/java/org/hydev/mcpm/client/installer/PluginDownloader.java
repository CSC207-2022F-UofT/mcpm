package org.hydev.mcpm.client.installer;

/**
  * Downloader for Spigot plugins
 */
public interface PluginDownloader {
    /**
     * Download a plugin
     *
     * @param pluginId      Spigot Plugin ID
     * @param pluginVersion Spigot Plugin Version ID
     * @param destination   File path to write to
     */
    void download(long pluginId, long pluginVersion, String destination);
}

