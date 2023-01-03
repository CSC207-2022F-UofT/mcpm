package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.UpdateController
import org.hydev.mcpm.client.display.presenters.LogUpdatePresenter
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Handles parsing related to the update command.
 *
 * @param controller A controller to dispatch an update command when invoked.
 */

data class UpdateParser(val controller: UpdateController) : CommandParser
{
    override val name = "update"
    override val description = "Updates plugins to the latest version."

    override fun configure(parser: Subparser)
    {
        // if (Constants.IS_MINECRAFT) {
        parser.addArgument("--load")
            .type(Boolean::class.javaPrimitiveType)
            .action(Arguments.storeTrue())
            .dest("load")
            .help("If true, updated plugins will be reloaded after the update.")
        // }
        parser.addArgument("--no-cache")
            .type(Boolean::class.javaPrimitiveType)
            .action(Arguments.storeTrue())
            .dest("no-cache")
            .help("If true, the cache will be skipped and database will be fetched again.")
        parser.addArgument("names")
            .nargs("*")
            .help("List of plugin names to update.")
    }

    override suspend fun run(details: Namespace, log: ILogger)
    {
        // Since log can change from invocation to invocation,
        // and I don't want UpdatePresenter to depend on ILogger,
        // I'll instantiate this every call.
        val presenter = LogUpdatePresenter(log)
        controller.update(
            details.getList("names"),
            details.getBoolean("load"),
            details.getBoolean("no-cache"),
            presenter
        )
    }
}
