package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.UnloadBoundary;

import java.util.List;
import java.util.function.Consumer;

/**
 * A command that handles plugin unloading operations. See UnloadEntry and UnloadParser.
 */
public class UnloadController {
    private final UnloadBoundary unloader;

    /**
     * Creates a UnloadCommand object with this specified UnloadBoundary to use when dispatched.
     *
     * @param loader The "unload boundary" to use in Command operation.
     */
    public UnloadController(UnloadBoundary loader) {
        this.unloader = loader;
    }

    /**
     * Unload plugins and output status to log.
     *
     * @param pluginNames A list of all plugin names to be unloaded.
     * @param log Callback for status for log events.
     */
    public void unload(List<String> pluginNames, Consumer<String> log) {
        for (var name : pluginNames) {
            try {
                unloader.unloadPlugin(name);

                log.accept(String.format("Plugin %s loaded", name));
            } catch (PluginNotFoundException e) {
                log.accept(String.format("Plugin %s not found", name));
            }
        }
    }
}
