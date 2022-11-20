package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.parsers.CommandParser;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.function.Consumer;

/**
 * Handles parsing command arguments into CommandEntry objects (to be dispatched to Controller).
 * For steps to add a new command, see Command.java.
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-30
 */
public class ArgsParser
{
    private final ArgumentParser parser;
    private final Consumer<String> log;

    /**
     * Creates a new ArgsParser object with a list of CommandParsers.
     *
     * @param allParsers A list of all CommandParser objects that this ArgsParser can parse.
     */
    public ArgsParser(List<CommandParser> allParsers, Consumer<String> log) {
        this.log = log;

        parser = ArgumentParsers
            .newFor("loader")
            .build()
            .defaultHelp(true);

        var parsers = parser.addSubparsers();

        for (var parser : allParsers) {
            var subparser = parser.configure(parsers);

            if (subparser != null) {
                subparser
                    .setDefault("handler", parser);
            }
        }
    }

    public void parse(String[] arguments) throws ArgumentParserException {
        parse(arguments, log);
    }

    /**
     * Parses arguments into a CommandEntry object.
     *
     * @param arguments A list of string arguments.
     *                  Example: [ "load", "pluginA", "pluginB" ].
     * @param log Logger
     * @throws ArgumentParserException Thrown when arguments are not parsed correctly.
     *                                 For default handling, pass this to ArgsParser#fail.
     */
    public void parse(String[] arguments, Consumer<String> log) throws ArgumentParserException {
        var namespace = parser.parseArgs(arguments);

        Object handleObject = namespace.get("handler");

        if (!(handleObject instanceof CommandParser handler)) {
            throw new ArgumentParserException("Unrecognized command.", parser);
        }

        handler.run(namespace, log);
    }

    /**
     * Writes the error message to stdout (along with help details if needed).
     *
     * @param e The error object that was caught from #parse.
     */
    public void fail(ArgumentParserException e) {
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);

        parser.handleError(e, printer);

        log.accept(writer.toString());
    }

    /**
     * Default help string that can be presented to the user if help dialog is needed.
     *
     * @return A string containing help information on each command.
     */
    public String help() {
        return parser.formatHelp();
    }

    /**
     * ArgsParser demo main.
     *
     * @param args Arguments are ignored.
     */
    public static void main(String[] args) {
        var parser = CommandsFactory.baseArgsParser();

        try {
            parser.parse(args);
        } catch (ArgumentParserException e) {
            parser.fail(e);
        }
    }
}
