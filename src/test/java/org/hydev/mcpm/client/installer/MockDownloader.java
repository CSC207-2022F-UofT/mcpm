package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.installer.input.InstallInput;

import java.io.File;
import java.util.ArrayList;

public class MockDownloader implements PluginDownloader{
    private final Downloader downloader;

    public MockDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public void download(String pluginName, long pluginId, long pluginVersion) {
        System.out.println("Download completed!");
    }
}
