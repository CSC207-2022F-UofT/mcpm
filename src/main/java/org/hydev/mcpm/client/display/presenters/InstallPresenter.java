package org.hydev.mcpm.client.display.presenters;

import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter;
import org.hydev.mcpm.client.installer.InstallResult;

import java.util.function.Consumer;

/**
 * Implementation to the ResultPresenter, display the result of installation of plugins
 */
public class InstallPresenter implements InstallResultPresenter {
    private final Consumer<String> log;

    /**
     * Instantiate Install Presenter
     *
     * @param log log string to the console
     */

    public InstallPresenter(Consumer<String> log) {
        this.log = log;
    }

    @Override
    public void displayResult(InstallResult installResult, String name) {
        this.log.accept(name + ": " + installResult.type().reason());
    }
}
