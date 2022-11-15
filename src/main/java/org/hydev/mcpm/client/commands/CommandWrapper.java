package org.hydev.mcpm.client.commands;

public class CommandWrapper<T extends CommandEntry> implements CommandResponder {
    private final Command<T> command;
    private final Class<T> type;

    @Override
    public boolean handles(CommandEntry input) {
        return type.isInstance(input);
    }

    @Override
    public void run(CommandEntry input) {
        if (type.isInstance(input)) {
            command.run(type.cast(input));
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
