package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.arguments.parsers.CommandParser;
import org.hydev.mcpm.client.arguments.parsers.EchoParser;
import org.hydev.mcpm.client.arguments.parsers.LoadParser;
import org.hydev.mcpm.client.arguments.parsers.ReloadParser;
import org.hydev.mcpm.client.arguments.parsers.UnloadParser;
import org.hydev.mcpm.client.commands.CommandResponder;
import org.hydev.mcpm.client.commands.CommandWrapper;
import org.hydev.mcpm.client.commands.Controller;
import org.hydev.mcpm.client.commands.entries.EchoCommand;
import org.hydev.mcpm.client.commands.entries.LoadCommand;
import org.hydev.mcpm.client.commands.entries.ReloadCommand;
import org.hydev.mcpm.client.commands.entries.UnloadCommand;
import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.injector.ReloadBoundary;
import org.hydev.mcpm.client.injector.UnloadBoundary;

import java.util.List;

public class CommandsFactory {
    private CommandsFactory() { }

    public static List<CommandParser> serverParsers() {
        return List.of(
            new EchoParser(),
            new LoadParser(),
            new ReloadParser(),
            new UnloadParser()
        );
    }

    public static List<CommandResponder> serverCommands() {
        var plugins = new PluginLoader();

        return List.of(
            CommandWrapper.wrap(new EchoCommand()),
            CommandWrapper.wrap(new LoadCommand(plugins)),
            CommandWrapper.wrap(new UnloadCommand(plugins)),
            CommandWrapper.wrap(new ReloadCommand(plugins))
        );
    }

    public static ArgsParser serverArgsParser() {
        return new ArgsParser(serverParsers());
    }

    public static Controller serverController() {
        return new Controller(serverCommands());
    }
}
