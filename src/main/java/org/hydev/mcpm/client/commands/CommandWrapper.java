package org.hydev.mcpm.client.commands;

import java.util.function.Consumer;

/**
 * Adapter from a Command object to more general CommandResponder object.
 * Use CommandWrapper.wrap(Command) before passing a Command to controller.
 * This may be unnecessarily complicated, but this was done to dodge the issue of Controller casting to generic types.
 * This also allows for Commands that handle multiple different Entry objects, since the type is generic.
 *
 * @param <T> The type of the entry object for the underlying Command.
 */
public class CommandWrapper<T extends CommandEntry> implements CommandResponder {
    private final Command<T> command;
    private final Class<T> type;

    @Override
    public boolean handles(CommandEntry input) {
        return type.isInstance(input);
    }

    @Override
    public void run(CommandEntry input, Consumer<String> log) {
        if (type.isInstance(input)) {
            command.run(type.cast(input), log);
        }
    }

    CommandWrapper(Command<T> command) {
        this(command, command.type());
    }

    CommandWrapper(Command<T> command, Class<T> type) {
        this.command = command;
        this.type = type;
    }

    public static <T extends CommandEntry> CommandWrapper<T> wrap(Command<T> command) {
        return new CommandWrapper<T>(command);
    }
}
