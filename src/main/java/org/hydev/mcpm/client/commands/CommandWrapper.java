package org.hydev.mcpm.client.commands;

import java.util.function.Consumer;

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
