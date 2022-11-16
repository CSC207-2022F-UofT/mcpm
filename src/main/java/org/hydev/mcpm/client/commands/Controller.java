package org.hydev.mcpm.client.commands;

import java.util.List;
import java.util.function.Consumer;

public class Controller {
    private final List<CommandResponder> commands;

    public static class NoMatchingCommandException extends Exception {
        CommandEntry entry;

        NoMatchingCommandException(CommandEntry entry) {
            this.entry = entry;
        }
    }

    public Controller(List<CommandResponder> commands) {
        this.commands = commands;
    }

    public void queue(CommandEntry entry, Consumer<String> log) throws NoMatchingCommandException {
        var command = commands
            .stream()
            .filter(c -> c.handles(entry))
            .findFirst();

        if (command.isEmpty()) {
            throw new NoMatchingCommandException(entry);
        }

        command.get().run(entry, log);
    }
}
