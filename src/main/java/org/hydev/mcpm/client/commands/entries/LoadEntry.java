package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.CommandEntry;

import java.util.List;


/**
 * Input object for LoadCommand.
 *
 * @param pluginNames The names of the plugins to be loaded.
 */
public record LoadEntry(
    List<String> pluginNames
) implements CommandEntry { }
