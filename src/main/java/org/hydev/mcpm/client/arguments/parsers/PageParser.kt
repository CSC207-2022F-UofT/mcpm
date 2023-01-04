package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.PageBoundary
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Parser for the pagination command
 */
data class PageParser(val controller: PageBoundary) : CommandParser
{
    override val name = "page"
    override val description = ""

    override fun configure(parser: Subparser)
    {
        parser.addArgument("page").type(Int::class.java)
            .help("Number of the page you want to view")
    }

    override suspend fun run(details: Namespace, log: ILogger)
    {
        val page = controller.formatPage(details.getInt("page"))
        if (page == null)
        {
            log.print("&cNo pages available.")
            return
        }
        log.print(page)
    }
}
