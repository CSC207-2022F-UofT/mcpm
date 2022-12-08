package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.Downloader;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Supplier;

import static org.hydev.mcpm.utils.GeneralUtils.concatUri;

/**
 * Plugin downloader for the MCPM Plugin Repository
 */

public class SpigotPluginDownloader implements PluginDownloader {
    private final Downloader downloader;
    private final Supplier<URI> host;

    /**
     * Initialize the Spigot Plugin Downloader
     *
     * @param downloader The file downloader
     * @param host Base URL provider
     * */
    public SpigotPluginDownloader(Downloader downloader, Supplier<URI> host) {
        this.downloader = downloader;
        this.host = host;
    }

    /**
     * Download the plugin according to its filepath.
     *
     * @param pluginName   The name of the plugin.
     * @param pluginId      The id of the plugin to download.
     * @param pluginVersion The associated plugin version.
     */
    @Override
    public void download(String pluginName, long pluginId, long pluginVersion) {
        String filePath = "plugins/" + pluginName + ".jar";

        try {
            // I'm sorry, this is an important for the update API.
            Files.createDirectories(Paths.get(filePath).getParent());
        } catch (IOException e) {
            /* ignore */
        }

        String url = constructUrl(pluginId, pluginVersion);
        downloader.downloadFile(url, filePath);
    }

    /**
     * Construct Url as a plugin source for installation
     *
     * @param pluginId Spigot Plugin ID
     * @param pluginVersion Spigot Plugin Version ID
     */
    private String constructUrl(long pluginId, long pluginVersion) {
        var path = String.format("pkgs/spiget/%s/%s/release.jar", pluginId, pluginVersion);
        return concatUri(host.get(), path).toString();
    }
}
