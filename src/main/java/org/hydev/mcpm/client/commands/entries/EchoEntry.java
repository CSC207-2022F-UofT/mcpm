package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.CommandEntry;

/**
 * Input object for the echo command. See EchoParser for a how to create this object from arguments.
 *
 * @param text The text to be echoed to console.
 */
public record EchoEntry(
    String text
) implements CommandEntry { }
