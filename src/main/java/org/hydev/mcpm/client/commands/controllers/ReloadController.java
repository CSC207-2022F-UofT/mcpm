package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.interaction.ILogger;
import org.hydev.mcpm.client.loader.PluginNotFoundException;
import org.hydev.mcpm.client.loader.ReloadBoundary;

import java.util.List;

/**
 * A command that handles plugin reloading operations. See ReloadEntry and ReloadParser.
 */
public record ReloadController(ReloadBoundary reloader) {
    /**
     * Hot reload plugins and output status to log.
     *
     * @param pluginNames A list of all plugin names to be reloaded.
     * @param log Callback for status for log events.
     */
    public void reload(List<String> pluginNames, ILogger log) {
        for (var name : pluginNames) {
            try {
                log.print(String.format("&6Reloading %s...", name));
                reloader.reloadPlugin(name);
                log.print(String.format("&aPlugin %s reloaded!", name));
            } catch (PluginNotFoundException e) {
                log.print(String.format("&cError: Plugin %s not found", name));
            }
        }
    }
}
