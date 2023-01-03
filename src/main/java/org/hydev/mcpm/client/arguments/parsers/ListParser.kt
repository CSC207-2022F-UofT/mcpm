package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.ListController
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Command parser for List command
 */

data class ListParser(val controller: ListController) : CommandParser
{
    override val name get() = "list"
    override val description get() = "List installed plugins"

    override fun configure(parser: Subparser)
    {
        parser.addArgument("type").choices("all", "manual", "automatic", "outdated").setDefault("all").nargs("?")
    }

    override suspend fun run(details: Namespace, log: ILogger)
    {
        controller.listAll(details.getString("type"), log)
    }
}
