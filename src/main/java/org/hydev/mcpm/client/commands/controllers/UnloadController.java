package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.interaction.ILogger;
import org.hydev.mcpm.client.loader.PluginNotFoundException;
import org.hydev.mcpm.client.loader.UnloadBoundary;

import java.util.List;

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
    public void unload(List<String> pluginNames, ILogger log) {
        for (var name : pluginNames) {
            try {
                log.print(String.format("&6Unloading %s...", name));
                unloader.unloadPlugin(name);
                log.print(String.format("&aPlugin %s unloaded!", name));
            } catch (PluginNotFoundException e) {
                log.print(String.format("&cError: Plugin %s not found", name));
            }
        }
    }
}
