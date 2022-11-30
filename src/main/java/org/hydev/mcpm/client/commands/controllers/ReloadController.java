package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.ReloadBoundary;

import java.util.List;
import java.util.function.Consumer;

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
    public void reload(List<String> pluginNames, Consumer<String> log) {
        for (var name : pluginNames) {
            try {
                log.accept(String.format("&6Reloading %s...", name));
                reloader.reloadPlugin(name);
                log.accept(String.format("&aPlugin %s reloaded!", name));
            } catch (PluginNotFoundException e) {
                log.accept(String.format("&cError: Plugin %s not found", name));
            }
        }
    }
}
