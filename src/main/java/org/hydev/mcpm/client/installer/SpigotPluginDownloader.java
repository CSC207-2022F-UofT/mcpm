package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.Downloader;

import java.io.File;

/**
 * Plugin downloader for the MCPM Plugin Repository
 *
 * @author Rena (https://github.com/thudoan1706)
 * @since 2022-11-20
 */
public class SpigotPluginDownloader implements PluginDownloader {

    private final Downloader downloader;
    private final String baseUrl;

    public SpigotPluginDownloader(Downloader downloader) {
        this(downloader, "https://mcpm.hydev.org");
    }

    public SpigotPluginDownloader(Downloader downloader, String baseUrl) {
        this.downloader = downloader;
        this.baseUrl = baseUrl;
    }

    @Override
    public void download(long pluginId, long pluginVersion, String destination) {
        String url = constructUrl(pluginId, pluginVersion);
        downloader.downloadFile(url, new File(destination));
    }

    private String constructUrl(long pluginId, long pluginVersion) {
        return String.format("%s/pkgs/spiget/%s/%s/release.jar", baseUrl, pluginId, pluginVersion);
    }
}
