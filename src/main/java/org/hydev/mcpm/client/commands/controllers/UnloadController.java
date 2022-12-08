package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.loader.PluginNotFoundException;
import org.hydev.mcpm.client.loader.UnloadBoundary;

import java.util.List;
import java.util.function.Consumer;

/**
 * A command that handles plugin unloading operations. See UnloadEntry and UnloadParser.
 */
public record UnloadController(UnloadBoundary unloader) {
    /**
     * Unload plugins and output status to log.
     *
     * @param pluginNames A list of all plugin names to be unloaded.
     * @param log Callback for status for log events.
     */
    public void unload(List<String> pluginNames, Consumer<String> log) {
        for (var name : pluginNames) {
            try {
                log.accept(String.format("&6Unloading %s...", name));
                unloader.unloadPlugin(name);
                log.accept(String.format("&aPlugin %s unloaded!", name));
            } catch (PluginNotFoundException e) {
                log.accept(String.format("&cError: Plugin %s not found", name));
            }
        }
    }
}
