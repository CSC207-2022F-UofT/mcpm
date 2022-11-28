package org.hydev.mcpm.client.installer.presenter;

/**
 * Interface for presenting the state of plugins
 *
 */
public interface ResultPresenter {
    /**
     * Display the string to the console
     *
     * @param message Description of plugin state
     */
    void displayResult(String message);
}
