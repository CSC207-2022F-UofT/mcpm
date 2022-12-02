package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.Downloader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Supplier;

import static org.hydev.mcpm.utils.GeneralUtils.concatUri;

/**
 * Plugin downloader for the MCPM Plugin Repository
 *
 * @author Rena (https://github.com/thudoan1706)
 * @since 2022-11-20
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
     * Download the plugin according to its filepath
     *
     * @param pluginId      The Id of the plugin
     * @param pluginVersion the plugin Version
     * @param destination   The filepath where the plugin will be installed to
     */
    @Override
    public void download(long pluginId, long pluginVersion, String destination) {
        try {
            // I'm sorry, this is an important for the update API.
            Files.createDirectories(Paths.get(destination).getParent());
        } catch (IOException e) {
            /* ignore */
        }

        String url = constructUrl(pluginId, pluginVersion);
        downloader.downloadFile(url, new File(destination));
    }

    /**
     * Construct Url as a plugin source for installation
     *
     * @param pluginId Spigot Plugin ID
     * @param pluginVersion Spigot Plugin Version ID
     */
    private String constructUrl(long pluginId, long pluginVersion) {

        return concatUri(host.get(), String.format("pkgs/spiget/%s/%s/release.jar", pluginId, pluginVersion)).toString();
    }
}
