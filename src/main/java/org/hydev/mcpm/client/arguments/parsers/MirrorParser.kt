package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.MirrorController
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Parser for the mirror selector command
 *
 * @param controller Mirror controller
 */
data class MirrorParser(val controller: MirrorController) : CommandParser
{
    override val name = "mirror"
    override val description = "Select a source (mirror) to download plugins from"

    override suspend fun run(details: Namespace, log: ILogger)
    {
        when (val op = details.getString("op"))
        {
            "ping" -> controller.ping(details.getBoolean("refresh"), log)
            "select" -> controller.select(details.getString("host"), log)
            else -> throw UnsupportedOperationException("Unknown operation: $op")
        }
    }

    override fun configure(parser: Subparser)
    {
        val sub = parser.addSubparsers()

        val ping = sub.addParser("ping")
            .help("Ping mirrors")
        ping.addArgument("-r", "--refresh").action(Arguments.storeTrue()).dest("refresh")
            .help("Refresh the mirror list database")
        ping.setDefault("op", "ping")

        val sel = sub.addParser("select")
            .help("Select a mirror")
        sel.addArgument("host").nargs("?")
            .help("Host of the mirror")
        sel.setDefault("op", "select")
    }
}
