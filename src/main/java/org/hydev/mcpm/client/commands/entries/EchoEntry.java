package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.CommandEntry;

public record EchoEntry(
    String text
) implements CommandEntry { }
