package org.hydev.mcpm.client.installer.presenter;

import org.hydev.mcpm.client.installer.InstallResult;

/**
 * Interface for presenting the state of plugins
 *
 */
public interface InstallResultPresenter {
    /**
     * Display the string to the console
     *
     * @param installResult state of the installation
     * @param name Description of plugin state
     */
    void displayResult(InstallResult installResult, String name);
}
