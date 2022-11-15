package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.commands.CommandResponder;
import org.hydev.mcpm.client.commands.CommandWrapper;
import org.hydev.mcpm.client.commands.Controller;
import org.hydev.mcpm.client.commands.entries.EchoCommand;

import java.util.List;

public class CommandsFactory {
    private CommandsFactory() { }

    public static List<CommandParser> serverParsers() {
        return List.of(
            new EchoParser()
        );
    }

    public static List<CommandResponder> serverCommands() {
        return List.of(
            CommandWrapper.wrap(new EchoCommand())
        );
    }

    public static ArgsParser serverArgsParser() {
        return new ArgsParser(serverParsers());
    }

    public static Controller serverController() {
        return new Controller(serverCommands());
    }
}
