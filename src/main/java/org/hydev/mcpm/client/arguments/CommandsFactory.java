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
import org.hydev.mcpm.client.injector.PluginLoader;

import java.util.List;
import java.util.stream.Stream;

/**
 * Class that produces default implementations of the Controller and ArgsParser classes.
 * These classes are used to handle commands.
 * Checkout Command.java for a process on adding a new command!
 */
public class CommandsFactory {
    // No instantiation.
    private CommandsFactory() { }

    /**
     * Creates a list of general parsers for the ArgsParser class.
     *
     * @return Returns a list of argument parsers that work in any environment (Server & CLI).
     */
    public static List<CommandParser> baseParsers() {
        /*
         * Add general parsers to this list!
         * All versions of MCPM will have access to these parsers.
         * If you're not sure if your command is server-only, add it to this list!
         */
        return List.of(
            new EchoParser()
        );
    }

    /**
     * Creates a list of server-only parsers for the ArgsParser class.
     *
     * @return Returns a list of argument parsers that require the Server (Minecraft Bukkit Plugin) environment.
     */
    public static List<CommandParser> serverParsers() {
        /*
         * Add server-only parsers to this list!
         * Server only commands will not show up in the MCPM CLI.
         * They'll only be accessible when a user tries to execute a command from the MCPM Server Plugin (in-game).
         */
        var serverOnly = List.of(
            new LoadParser(),
            new ReloadParser(),
            new UnloadParser()
        );

        return Stream.concat(baseParsers().stream(), serverOnly.stream()).toList();
    }

    /**
     * Creates a list of general Commands for the Controller class.
     *
     * @return Returns a list of commands that work in any environment (Server & CLI).
     */
    public static List<CommandResponder> baseCommands() {
        /*
         * Add general commands to this list!
         * All versions of MCPM will have access to these commands.
         * If you're not sure if your command is server-only, add it to this list!
         */
        return List.of(
            CommandWrapper.wrap(new EchoCommand())
        );
    }

    /**
     * Creates a list of server-only commands for the Controller class.
     *
     * @return Returns a list of argument parsers that require the Server (Minecraft Bukkit Plugin) environment.
     */
    public static List<CommandResponder> serverCommands() {
        var plugins = new PluginLoader();

        /*
         * Add server-only commands to this list!
         * Server only commands will not show up in the MCPM CLI.
         * They'll only be accessible when a user tries to execute a command from the MCPM Server Plugin (in-game).
         */
        var serverOnly = List.of(
            CommandWrapper.wrap(new LoadCommand(plugins)),
            CommandWrapper.wrap(new UnloadCommand(plugins)),
            CommandWrapper.wrap(new ReloadCommand(plugins))
        );

        return Stream.concat(baseCommands().stream(), serverOnly.stream()).toList();
    }

    /**
     * Creates an ArgsParser object that has general parsers (works in any environment).
     *
     * @return An ArgsParser object. Invoke ArgsParser#parse to see more.
     */
    public static ArgsParser baseArgsParser() {
        return new ArgsParser(baseParsers());
    }

    /**
     * Creates an Controller object that has general commands (works in any environment).
     *
     * @return An Controller object. Invoke Controller#queue to see more.
     */
    public static Controller baseController() {
        return new Controller(baseCommands());
    }

    /**
     * Creates an ArgsParser object that has all (CLI & Server) parsers.
     *
     * @return An ArgsParser object. Invoke ArgsParser#parse to see more.
     */
    public static ArgsParser serverArgsParser() {
        return new ArgsParser(serverParsers());
    }

    /**
     * Creates an Controller object that has all (CLI & Server) parsers.
     *
     * @return An Controller object. Invoke Controller#queue to see more.
     */
    public static Controller serverController() {
        return new Controller(serverCommands());
    }
}
