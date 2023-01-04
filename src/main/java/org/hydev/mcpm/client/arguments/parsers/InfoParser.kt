package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.InfoController
import org.hydev.mcpm.client.commands.presenters.InfoPresenter
import org.hydev.mcpm.client.interaction.ILogger
import org.hydev.mcpm.client.loader.PluginNotFoundException

/**
 * Command parser for the info use case
 */
data class InfoParser(val controller: InfoController, val presenter: InfoPresenter) : CommandParser
{
    override val name = "info"
    override val description = "Show the info of an installed plugin"

    override suspend fun run(details: Namespace, log: ILogger)
    {
        val name = details.getString("name")
        try
        {
            presenter.present(controller.info(name), log)
        }
        catch (e: PluginNotFoundException)
        {
            log.print(String.format("&cCannot find plugin '%s'", name))
        }
    }

    override fun configure(parser: Subparser)
    {
        parser.addArgument("name").help("Name of the plugin")
    }
}
