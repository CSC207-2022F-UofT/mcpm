package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;
import org.hydev.mcpm.client.arguments.parsers.CommandParser;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
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
    private final String help;
    private final ArgumentParser parser;
    private final Consumer<String> log;
    private final List<CommandParser> subparsers;

    /**
     * Creates a new ArgsParser object with a list of CommandParsers.
     *
     * @param allParsers A list of all CommandParser objects that this ArgsParser can parse.
     */
    public ArgsParser(List<CommandParser> allParsers, Consumer<String> log) {
        this.log = log;
        this.subparsers = allParsers;

        parser = ArgumentParsers.newFor("mcpm").addHelp(false).build();

        var parsers = parser.addSubparsers();

        for (var parser : allParsers) {
            var subparser = parsers.addParser(parser.name(), false);
            parser.configure(subparser);

            subparser.setDefault("handler", parser);
            subparser.addArgument("-h", "--help").action(new PrintHelpAction(subparser));
        }

        // Create help string and help command
        help = "mcpm: Minecraft Plugin Package Manager\n" + String.join("", allParsers.stream()
            .map(it -> String.format("&f/mcpm %s &6- %s&r\n", it.name(), it.description())).toList()) +
            "To view the help message of a command, use /mcpm <command> -h";
        var helpSub = parsers.addParser("help", false);
        helpSub.setDefault("handler", (CommandHandler) (args, log1) -> log1.accept(help));
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
        // If no args are present, add help
        if (arguments.length == 0) {
            log.accept(help());
            return;
        }

        try {
            var namespace = parser.parseArgs(arguments);

            Object handleObject = namespace.get("handler");

            if (!(handleObject instanceof CommandHandler handler)) {
                throw new ArgumentParserException("Unrecognized command.", parser);
            }

            handler.run(namespace, log);
        }
        catch (HelpException e) {
            log.accept("&e" + e.help());
        }
    }

    /**
     * Writes the error message to stdout (along with help details if needed).
     *
     * @param e The error object that was caught from #parse.
     */
    public void fail(ArgumentParserException e) {
        this.fail(e, log);
    }

    /**
     * Writes the error message to stdout (along with help details if needed).
     *
     * @param e The error object that was caught from #parse.
     */
    public void fail(ArgumentParserException e, Consumer<String> log) {
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);

        parser.handleError(e, printer);

        log.accept("&c" + writer);
    }

    /**
     * Default help string that can be presented to the user if help dialog is needed.
     *
     * @return A string containing help information on each command.
     */
    public String help() {
        return help;
    }

    public List<CommandParser> getRawSubparsers() {
        return subparsers;
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
