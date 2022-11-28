package org.hydev.mcpm.client.installer.presenter;

import org.hydev.mcpm.client.installer.InstallResult;

import java.util.function.Consumer;

/**
 * Implementation to the ResultPresenter, display the result of installation of plugins
 */
public class InstallPresenter implements ResultPresenter {

    private Consumer<String> log;
    /**
     * Instantiate Install Presenter
     *
     * @param log log string to the console
     */

    public InstallPresenter(Consumer<String> log) {
        this.log = log;
    }

    @Override
    public void displayResult(String message) {
        this.log.accept(message);
    }
}
