package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.installer.output.InstallResult;

import java.util.List;
import java.util.function.Consumer;

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
    void displayResult(List<InstallResult> results, Consumer<String> log);
}
