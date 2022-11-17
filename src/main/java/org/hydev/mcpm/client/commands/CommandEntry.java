package org.hydev.mcpm.client.commands;

/**
 * Empty interface that designates a CommandEntry object.
 * If you want your Command to take an object as an input, make sure that class implements CommandEntry.
 * This is generally to avoid reusing objects as CommandEntry objects,
 * since CommandEntry types are used to identify which command is being executed.
 */
public interface CommandEntry {
    /* empty */
}
