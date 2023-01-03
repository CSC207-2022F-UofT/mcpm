package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.LoadController
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Argument parser for LoadCommand. See LoadEntry.
 */

data class LoadParser(val controller: LoadController) : CommandParser
{
    override val name = "load"
    override val description = "Load a plugin in the plugins folder"

    override fun configure(parser: Subparser)
    {
        parser.addArgument("plugins").dest("plugins").nargs("+")
            .help("Name of the plugins to load")
    }

    override suspend fun run(details: Namespace, log: ILogger)
    {
        controller.load(details.getList("plugins"), log)
    }
}
