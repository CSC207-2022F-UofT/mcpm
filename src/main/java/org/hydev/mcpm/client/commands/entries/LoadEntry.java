package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.CommandEntry;

import java.util.List;

public record LoadEntry(
    List<String> pluginNames
) implements CommandEntry { }
