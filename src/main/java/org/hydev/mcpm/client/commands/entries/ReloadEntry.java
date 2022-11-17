package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.CommandEntry;

import java.util.List;

/**
 * Input object for ReloadCommand.
 *
 * @param pluginNames The names of the plugins to be reloaded.
 */
public record ReloadEntry(
    List<String> pluginNames
) implements CommandEntry { }
