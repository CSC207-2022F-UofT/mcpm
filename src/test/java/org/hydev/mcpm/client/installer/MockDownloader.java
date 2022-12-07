package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.Downloader;
import org.hydev.mcpm.client.installer.input.InstallInput;

import java.io.File;
import java.util.ArrayList;

/**
 * Mock implementation of the PluginDownloader interface.
 *
 */
public class MockDownloader implements PluginDownloader {
    private final Downloader downloader;

    /**
     * Mock Downloader
     *
     * @param downloader File downloader (it will be null)
     */
    public MockDownloader(Downloader downloader) {
        this.downloader = downloader;
    }

    @Override
    public void download(String pluginName, long pluginId, long pluginVersion) {
        System.out.println("Download completed!");
    }
}
