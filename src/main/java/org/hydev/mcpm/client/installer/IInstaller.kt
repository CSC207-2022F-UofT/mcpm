package org.hydev.mcpm.client.installer

import org.hydev.mcpm.client.installer.input.InstallInput
import org.hydev.mcpm.client.installer.output.InstallResult
import org.hydev.mcpm.client.interaction.ILogger

/**
 * Interface for installing plugin to the jar file.
 */
interface IInstaller
{
    /**
     * Install a plugin
     *
     * @param installInput Options
     */
    suspend fun install(installInput: InstallInput, log: ILogger): List<InstallResult>
}
