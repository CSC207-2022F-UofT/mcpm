package org.hydev.mcpm.client.arguments

import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.inf.*
import org.hydev.mcpm.client.arguments.parsers.CommandHandler
import org.hydev.mcpm.client.arguments.parsers.CommandParser
import org.hydev.mcpm.client.interaction.ILogger
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Handles parsing command arguments into CommandEntry objects (to be dispatched to Controller).
 * For steps to add a new command, see Command.java.
 */
class ArgsParser(val rawSubparsers: List<CommandParser>)
{
    val help: String
    val parser: ArgumentParser = ArgumentParsers.newFor("mcpm").addHelp(false).build()

    /**
     * Creates a new ArgsParser object with a list of CommandParsers.
     *
     * @param allParsers A list of all CommandParser objects that this ArgsParser can parse.
     */
    init
    {
        val parsers = parser.addSubparsers()
        for (parser in rawSubparsers)
        {
            val subparser = parsers.addParser(parser.name, false)
            parser.configure(subparser)
            subparser.setDefault("handler", parser)
            subparser.addArgument("-h", "--help").action(PrintHelpAction(subparser))
        }

        // Create help string and help command
        help = "mcpm: Minecraft Plugin Package Manager\n" +
            rawSubparsers.filter { it.description.isNotBlank() }
                .map { "&f/mcpm ${it.name} &6- ${it.description}&r\n" }.joinToString("") +
            "To view the help message of a command, use /mcpm <command> -h"

        val helpSub = parsers.addParser("help", false)
        helpSub.setDefault("handler", object : CommandHandler
        {
            override suspend fun run(details: Namespace, log: ILogger) = log.print(help)
        })
    }

    /**
     * Parses arguments into a CommandEntry object.
     *
     * @param arguments A list of string arguments. Example: [ "load", "pluginA", "pluginB" ].
     * @param log Logger
     * @throws ArgumentParserException Thrown when arguments are not parsed correctly.
     * For default handling, pass this to ArgsParser#fail.
     */
    @Throws(ArgumentParserException::class)
    suspend fun parse(arguments: Array<String>, log: ILogger)
    {
        // If no args are present, add help
        if (arguments.isEmpty())
        {
            log.print(help)
            return
        }

        try
        {
            val namespace = parser.parseArgs(arguments)
            val handleObject = namespace.get<CommandHandler>("handler")
                ?: throw ArgumentParserException("Unrecognized command.", parser)
            handleObject.run(namespace, log)
        }
        catch (e: HelpException)
        {
            log.print("&e" + e.help())
        }
    }

    /**
     * Writes the error message to stdout (along with help details if needed).
     *
     * @param e The error object that was caught from #parse.
     */
    fun fail(e: ArgumentParserException?, log: ILogger)
    {
        val writer = StringWriter()
        val printer = PrintWriter(writer)
        parser.handleError(e, printer)
        log.print("&c$writer")
    }
}
