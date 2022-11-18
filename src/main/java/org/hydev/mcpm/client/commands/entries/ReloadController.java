package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.ReloadBoundary;

import java.util.List;
import java.util.function.Consumer;

/**
 * A command that handles plugin reloading operations. See ReloadEntry and ReloadParser.
 */
public class ReloadController {
    private final ReloadBoundary reloader;

    /**
     * Creates a ReloadCommand object with this specified ReloadBoundary to use when dispatched.
     *
     * @param loader The relaod boundary to use in Command operation.
     */
    public ReloadController(ReloadBoundary loader) {
        this.reloader = loader;
    }

    /**
     * Hot reload plugins and output status to log.
     *
     * @param pluginNames A list of all plugin names to be reloaded.
     * @param log Callback for status for log events.
     */
    public void reload(List<String> pluginNames, Consumer<String> log) {
        for (var name : pluginNames) {
            try {
                reloader.reloadPlugin(name);
                log.accept(String.format("Plugin %s loaded", name));
            } catch (PluginNotFoundException e) {
                log.accept(String.format("Plugin %s not found", name));
            }
        }
    }
}
