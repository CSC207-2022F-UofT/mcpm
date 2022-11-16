package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.CommandEntry;

import java.util.List;

public record UnloadEntry(
    List<String> pluginNames
) implements CommandEntry { }
