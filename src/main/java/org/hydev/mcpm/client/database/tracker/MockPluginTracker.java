package org.hydev.mcpm.client.database.tracker;

import org.hydev.mcpm.client.local.LocalPluginTracker;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginYml;

import java.util.ArrayList;
import java.util.List;

/**
 * A MockPluginTracker that only stores the yml.
 */
public class MockPluginTracker extends LocalPluginTracker {
    private final List<PluginYml> ymls;

    /**
     * Creates a MockPluginTracker with the given plugins.
     *
     * @param plugins the given plugins
     */
    public MockPluginTracker(List<PluginModel> plugins) {
        ymls = new ArrayList<>();
        for (var plugin : plugins) {
            var version = plugin.getLatest();

            version.ifPresent(pluginVersion -> ymls.add(pluginVersion.meta()));
        }
    }

    @Override
    public List<PluginYml> listInstalled() {
        return ymls;
    }
}
