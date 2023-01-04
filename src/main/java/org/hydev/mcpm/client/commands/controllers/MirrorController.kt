package org.hydev.mcpm.client.commands.controllers

import org.hydev.mcpm.client.database.mirrors.MirrorSelectBoundary
import org.hydev.mcpm.client.display.presenters.Table
import org.hydev.mcpm.client.interaction.ILogger
import java.io.IOException

/**
 * Controller for the mirror selection command
 */
data class MirrorController(val boundary: MirrorSelectBoundary)
{
    /**
     * Ask the user to select a mirror
     *
     * @param refresh If we should refresh the database
     * @param log Logger
     */
    fun ping(refresh: Boolean, log: ILogger)
    {
        try
        {
            if (refresh) boundary.updateMirrors()
            val selected = boundary.selectedMirror

            // Display the top 20 results
            val ping = boundary.pingMirrors()
            log.print(Table(listOf(":Host", "Delay:", "Speed:"), ping.map { listOf(
                (if (selected.host == it.k.host) "&6> " else "&f  ") + it.k.host,
                formatPing(it.v.toLong()), formatSpeed(it.k.speed.toLong())
            ) }).toString())

            // User feedback
            log.print("You can use /mcpm mirror select <host> to select a mirror.")
        }
        catch (e: IOException)
        {
            log.print(String.format("&cUnexpected error during processing: %s", e))
        }
    }

    /**
     * Select a mirror
     *
     * @param host Hostname of the mirror
     * @param log Logger
     */
    fun select(host: String, log: ILogger)
    {
        try
        {
            val mirror = boundary.listAvailableMirrors().firstOrNull { it.host == host } ?:
                return log.print("&cNo mirror of the host $host is found")

            boundary.selectedMirror = mirror
            log.print("&aSuccessfully selected $host as the mirror source")
        }
        catch (e: IOException)
        {
            log.print("&cError occurred while writing the configuration file: $e")
        }
    }
}

/**
 * Format ping with colors
 *
 * @param ping Ping in milliseconds
 * @return Ping with colors
 */
private fun formatPing(ping: Long): String
{
    val color = if (ping <= 100) "&a" else if (ping <= 200) "&e" else "&c"
    return "$color$ping ms"
}

/**
 * Format speed with colors
 *
 * @param speed Speed in mbps
 * @return Speed with colors
 */
private fun formatSpeed(speed: Long): String
{
    val color = if (speed >= 750) "&a" else if (speed >= 500) "&e" else "&c"
    return "$color$speed mbps"
}
