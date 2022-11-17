package org.hydev.mcpm.client.commands;

import java.util.function.Consumer;

/**
 * Higher level CommandResponder object.
 * Try to use Command.java when possible.
 */
public interface CommandResponder {
    /**
     * Returns true when this command handles CommandEntry objects.
     *
     * @param input The command entry object being queried.
     * @return True if this command can take this object as input in run.
     */
    boolean handles(CommandEntry input);

    /**
     * Called when Controller is trying to invoke the command with the input.
     *
     * @param input The command entry object used for command input.
     * @param log General stream to log things to instead of Sys.Out.
     */
    void run(CommandEntry input, Consumer<String> log);
}
