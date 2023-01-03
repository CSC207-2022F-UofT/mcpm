package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.installer.output.InstallResult;
import org.hydev.mcpm.client.interaction.ILogger;

import java.util.List;

/**
 * Interface for presenting the state of plugins
 *
 */
public interface InstallResultPresenter {
    /**
     * Display the string to the console
     *
     * @param results Resulting state of the installation
     */
    void displayResult(List<InstallResult> results, ILogger log);
}
