package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.InstallController
import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter
import org.hydev.mcpm.client.interaction.ILogger
import org.hydev.mcpm.client.search.SearchPackagesType

/**
 * Handles parsing install arguments (to be dispatched to Controller).
 */

data class InstallParser(val controller: InstallController, val presenter: InstallResultPresenter) : CommandParser
{
    override val name = "install"
    override val description = "Download and install a plugin from the database"

    override fun configure(parser: Subparser)
    {
        parser.addArgument("name").dest("name")
            .help("The name of the plugin you want to install")
        parser.addArgument("--no-load").action(Arguments.storeTrue()).dest("noLoad")
            .help("Default load, use this option if you don't want to load after install")
    }

    override suspend fun run(details: Namespace, log: ILogger)
    {
        val name = details.getString("name")
        val result = controller.install(
            name,
            SearchPackagesType.BY_NAME,
            !details.getBoolean("noLoad")
        )
        presenter.displayResult(result, log)
    }
}
