package org.hydev.mcpm.client.installer;

/**
 * Mock implementation of the PluginDownloader interface.
 *
 */
public class MockDownloader implements PluginDownloader {
    public void download(String pluginName, long pluginId, long pluginVersion) {
        System.out.println("Download completed!");
    }
}
