package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.UnloadController
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Argument parser for UnloadCommand. See UnloadEntry.
 */

data class UnloadParser(val controller: UnloadController) : CommandParser
{
    override val name = "unload"
    override val description = "Unload a currently loaded plugin"

    override fun configure(parser: Subparser)
    {
        parser.addArgument("plugins").dest("plugins").nargs("+")
            .help("Name of the plugins to unload")
    }

    override suspend fun run(details: Namespace, log: ILogger)
    {
        controller.unload(details.getList("plugins"), log)
    }
}
