package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;

import java.util.List;
import java.util.function.Consumer;

/**
 * A command that handles plugin loading operations. See LoadEntry and LoadParser.
 */
public class LoadController {
    private final LoadBoundary loader;

    /**
     * Creates a LoadCommand object with this specified LoadBoundary to use when dispatched.
     *
     * @param loader The load boundary to use in Command operation.
     */
    public LoadController(LoadBoundary loader) {
        this.loader = loader;
    }


    /**
     * Load plugins and output status to log.
     *
     * @param pluginNames A list of all plugin names to be loaded.
     * @param log Callback for status for log events.
     */
    public void load(List<String> pluginNames, Consumer<String> log) {
        for (var name : pluginNames) {
            try {
                log.accept(String.format("&6Loading %s...", name));
                if (loader.loadPlugin(name)) {
                    log.accept(String.format("&aPlugin %s loaded", name));
                } else {
                    log.accept(String.format("&cError: Failed to load plugin %s", name));
                }
            } catch (PluginNotFoundException e) {
                log.accept(String.format("&cError: Plugin %s not found", name));
            }
        }
    }
}
