package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.UninstallController
import org.hydev.mcpm.client.commands.presenters.UninstallResultPresenter
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Command parser for the uninstallation use case
 */

data class UninstallParser(val controller: UninstallController, val presenter: UninstallResultPresenter) : CommandParser
{
    override val name = "uninstall"
    override val description = "Uninstall a plugin from file system"

    override fun configure(parser: Subparser)
    {
        parser.addArgument("name")
            .help("Name of the plugin to uninstall")
        parser.addArgument("-n", "--no-recursive")
            .action(Arguments.storeFalse())
            .setDefault(true)
            .dest("recursive")
            .help("Recursively remove orphan dependencies")
    }

    override suspend fun run(details: Namespace, log: ILogger)
    {
        val name = details.getString("name")

        // Uninstall
        val result = controller.uninstall(name, details.getBoolean("recursive"))
        presenter.displayResult(name, result, log)
    }
}
