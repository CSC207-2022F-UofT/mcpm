package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.Downloader;

import java.io.File;

public class SpigotPluginDownloader implements PluginDownloader {

    private final Downloader downloader;
    private final String baseURL;

    public SpigotPluginDownloader(Downloader downloader) {
        this.downloader = downloader;
        this.baseURL = "https://mcpm.hydev.org";
    }

    public SpigotPluginDownloader(Downloader downloader, String baseURL) {
        this.downloader = downloader;
        this.baseURL = baseURL;
    }

    @Override
    public void download(long pluginId, long pluginVersion, String destination) {
        String url = constructURL(pluginId, pluginVersion);
        downloader.downloadFile(url, new File(destination));
    }

    public String constructURL(long pluginId, long pluginVersion) {
        String url = baseURL + "/pkgs/spiget/" + pluginId + "/" + pluginVersion + "/release.jar";
        return url;
    }
}
