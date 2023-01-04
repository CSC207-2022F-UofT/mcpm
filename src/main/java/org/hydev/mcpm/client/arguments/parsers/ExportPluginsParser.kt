package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.ExportController
import org.hydev.mcpm.client.export.ExportPluginsInput
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Parser for the ExportPluginsBoundary interface.
 */

data class ExportPluginsParser(val controller: ExportController) : CommandParser
{
    override val name = "export"
    override val description = "Export plugin configuration"

    override fun configure(parser: Subparser)
    {
        parser.addArgument("type").nargs("?").choices("pastebin", "file", "literal")
            .setDefault("pastebin") // type of output
            .type(String::class.java).dest("type")
        parser.addArgument("out").nargs("?")
            .type(String::class.java).dest("out")
    }

    override suspend fun run(details: Namespace, log: ILogger)
    {
        controller.export(ExportPluginsInput(details.get("type"), details.get("out")), log)
    }
}
