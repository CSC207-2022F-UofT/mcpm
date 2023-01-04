package org.hydev.mcpm.client.commands.controllers

import org.hydev.mcpm.client.installer.IInstaller
import org.hydev.mcpm.client.installer.input.InstallInput
import org.hydev.mcpm.client.installer.output.InstallResult
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Controller class for the installation use case.
 */
data class InstallController(val boundary: IInstaller)
{
    /**
     * Install the plugin
     *
     * @param names Plugin names to install
     * @param ids Plugin ids to install
     * @param load Whether to load after installing
     * @return Results
     */
    suspend fun install(names: List<String>, ids: List<Long>, load: Boolean, log: ILogger): List<InstallResult>
    {
        val input = InstallInput(names, ids, load, true)
        return boundary.install(input, log)
    }
}
