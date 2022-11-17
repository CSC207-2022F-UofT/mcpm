package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.CommandEntry;

import java.util.List;

/**
 * Input object for UnloadCommand.
 *
 * @param pluginNames The names of the plugins to be unloaded.
 */
public record UnloadEntry(
    List<String> pluginNames
) implements CommandEntry { }
