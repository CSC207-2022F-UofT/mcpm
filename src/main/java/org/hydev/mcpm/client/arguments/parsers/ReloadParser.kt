package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.ReloadController
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Argument parser for ReloadCommand. See ReloadEntry.
 */
data class ReloadParser(val controller: ReloadController) : CommandParser
{
    override val name = "reload"
    override val description = "Reload a currently loaded plugin"

    override fun configure(parser: Subparser)
    {
        parser.addArgument("plugins").dest("plugins").nargs("+")
            .help("Name of the plugins to reload")
    }

    override suspend fun run(details: Namespace, log: ILogger)
    {
        controller.reload(details.get("plugins"), log)
    }
}
