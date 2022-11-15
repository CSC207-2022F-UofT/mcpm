package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.CommandEntry;
import org.hydev.mcpm.client.commands.CommandWrapper;
import org.hydev.mcpm.client.commands.Controller;
import org.hydev.mcpm.client.commands.entries.EchoCommand;
import org.hydev.mcpm.client.commands.entries.EchoEntry;

import java.util.List;

/**
 * Static collection of argument parsers for the command line
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-30
 */
public class ArgsParser
{
    private final ArgumentParser parser;

    public ArgsParser(List<CommandParser> allParsers) {
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

    public CommandEntry parse(String[] arguments) throws ArgumentParserException {
        var namespace = parser.parseArgs(arguments);

        Object handleObject = namespace.get("handler");

        if (!(handleObject instanceof CommandParser handler)) {
            throw new ArgumentParserException("Unrecognized command.", parser);
        }

        return handler.build(namespace);
    }

    public void fail(ArgumentParserException e) {
        parser.handleError(e);
    }

    public String help() {
        return parser.formatHelp();
    }

    public static void main(String[] args) {
        var parser = CommandsFactory.serverArgsParser();
        var controller = CommandsFactory.serverController();

        try {
            var entry = parser.parse(args);
            controller.queue(entry);
        } catch (ArgumentParserException e) {
            parser.fail(e);
        } catch (Controller.NoMatchingCommandException e) {
            e.printStackTrace();
        }
    }
}
