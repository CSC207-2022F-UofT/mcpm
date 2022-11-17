package org.hydev.mcpm.client.commands;

import java.util.List;
import java.util.function.Consumer;

/**
 * Handles dispatching and executing CommandEntry objects.
 * For steps to add a new command, see Command.java.
 */
public class Controller {
    private final List<CommandResponder> commands;

    /**
     * Thrown when an Entry passed to `queue` does not match any commands in its command list.
     */
    public static class NoMatchingCommandException extends Exception {
        private final CommandEntry entry;

        /**
         * Returns the CommandEntry object that did not match any Command.
         *
         * @return A CommandEntry object.
         */
        public CommandEntry getEntry() {
            return entry;
        }

        /**
         * Creates a new NoMatchingCommandException.
         *
         * @param entry The CommandEntry object that didn't match any command in the command list.
         */
        public NoMatchingCommandException(CommandEntry entry) {
            this.entry = entry;
        }
    }

    /**
     * Creates a new Controller with a specified command list.
     * To create Controllers with a default command list, see CommandsFactory.java#baseController().
     *
     * @param commands The command list, which will be searched
     *                 when a Queue object is passed to Controller.
     */
    public Controller(List<CommandResponder> commands) {
        this.commands = commands;
    }

    /**
     * Queues and eventually runs a CommandEntry object.
     *
     * @param entry The Command Entry object to be dispatched (contains inputs for the Command).
     * @param log The handler for log operations.
     *            Will be passed down to the Command to list all user strings.
     *            We encourage commands to pass relevant OutputBoundaries through their CommandEntry.
     * @throws NoMatchingCommandException Thrown if entry cannot be handled by any Command in the command list.
     */
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
