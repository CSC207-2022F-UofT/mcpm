package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.InstallController
import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Handles parsing install arguments (to be dispatched to Controller).
 */

data class InstallParser(val controller: InstallController, val presenter: InstallResultPresenter) : CommandParser
{
    override val name = "install"
    override val description = "Download and install a plugin from the database"

    override fun configure(parser: Subparser)
    {
        parser.addArgument("names").nargs("+")
            .help("The names of the plugin you want to install")
        parser.addArgument("-i", "--id").action(Arguments.storeTrue()).dest("id")
            .help("Treat names as ids instead")
        parser.addArgument("-n", "--no-load").action(Arguments.storeTrue()).dest("noLoad")
            .help("Disable automatic loading after install")
    }

    override suspend fun run(details: Namespace, log: ILogger)
    {
        val names = details.getList<String>("names")
        var ids: List<Long> = listOf()
        if (details.getBoolean("id")) {
            ids = names.map { it.toLong() }
            names.clear()
        }

        val result = controller.install(names, ids, !details.getBoolean("noLoad"), log)
        presenter.displayResult(result, log)
    }
}
