package org.hydev.mcpm.client.arguments.parsers

import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.commands.controllers.RefreshController
import org.hydev.mcpm.client.interaction.ILogger
import java.io.IOException

/**
 * Refresh the database cache and mirror list
 */
data class RefreshParser(val controller: RefreshController) : CommandParser
{
    override val name = "refresh"
    override val description = "Refresh cached plugin database"

    override fun configure(parser: Subparser) {}

    override suspend fun run(details: Namespace, log: ILogger)
    {
        try
        {
            controller.refresh()
            log.print("&aDatabase refreshed successfully!")
        }
        catch (e: IOException)
        {
            log.print("&cDatabase refresh failed: " + e.message)
        }
    }
}
