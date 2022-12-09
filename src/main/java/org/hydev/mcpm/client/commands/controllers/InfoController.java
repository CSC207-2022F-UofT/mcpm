package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.loader.PluginNotFoundException;
import org.hydev.mcpm.client.models.PluginYml;

/**
 * Controller for the info use case
 */
public record InfoController(PluginTracker tracker)
{
    /**
     * Run the info command
     *
     * @param name Name of the plugin
     *
     */
    public PluginYml info(String name) throws PluginNotFoundException
    {
        return tracker.listInstalled().stream().filter(it -> it.name().equalsIgnoreCase(name)).findFirst()
            .orElseThrow(() -> new PluginNotFoundException(String.format("Cannot find plugin '%s'", name)));
    }
}
