package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.ImportController
import org.hydev.mcpm.client.export.ImportInput
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Parser for the import use case
 */

data class ImportParser(val controller: ImportController) : CommandParser
{
    override val name = "import"
    override val description = "Import a plugins config from a previous export"

    override fun configure(parser: Subparser)
    {
        parser.addArgument("type").nargs("?").choices("pastebin", "file", "literal")
            .setDefault("pastebin") // type of input
            .type(String::class.java).dest("type") // of type OutputStream
        parser.addArgument("input")
            .type(String::class.java).dest("input")
    }

    override suspend fun run(details: Namespace, log: ILogger)
    {
        controller.importPlugins(ImportInput(details.get("type"), details.get("input")), log)
    }
}
