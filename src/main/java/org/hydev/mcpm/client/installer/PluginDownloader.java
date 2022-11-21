package org.hydev.mcpm.client.installer;

/**
<<<<<<< HEAD
 * Downloader for Spigot plugins
 *
 * @author Rena (https://github.com/thudoan1706)
 * @since 2022-11-20
=======
 * Interface for plugin downloader.
 * @since 2022-11-18
>>>>>>> bec5e2f (add documentation)
 */
public interface PluginDownloader {
    /**
     * Download a plugin
     *
     * @param pluginId Spigot Plugin ID
     * @param pluginVersion Spigot Plugin Version ID
     * @param destination File path to write to
     */
    void download(long pluginId, long pluginVersion, String destination);
}

