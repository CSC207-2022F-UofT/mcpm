package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.interaction.ILogger;
import org.hydev.mcpm.client.loader.LoadBoundary;
import org.hydev.mcpm.client.loader.PluginNotFoundException;

import java.util.List;

/**
 * A command that handles plugin loading operations. See LoadEntry and LoadParser.
 */
public record LoadController(LoadBoundary loader) {
    /**
     * Load plugins and output status to log.
     *
     * @param pluginNames A list of all plugin names to be loaded.
     * @param log Callback for status for log events.
     */
    public void load(List<String> pluginNames, ILogger log) {
        for (var name : pluginNames) {
            try {
                log.print(String.format("&6Loading %s...", name));
                if (loader.loadPlugin(name)) {
                    log.print(String.format("&aPlugin %s loaded", name));
                } else {
                    log.print(String.format("&cError: Failed to load plugin %s", name));
                }
            } catch (PluginNotFoundException e) {
                log.print(String.format("&cError: Plugin %s not found", name));
            }
        }
    }
}
