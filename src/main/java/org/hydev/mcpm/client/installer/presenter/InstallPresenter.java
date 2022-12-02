package org.hydev.mcpm.client.installer.presenter;

import org.hydev.mcpm.client.installer.InstallResult;

import java.util.function.Consumer;

/**
 * Implementation to the ResultPresenter, display the result of installation of plugins
 */
public class InstallPresenter implements InstallResultPresenter {

    private Consumer<String> log;

    private InstallResult installResult;

    private String name;
    /**
     * Instantiate Install Presenter
     *
     * @param log log string to the console
     */

    public InstallPresenter(Consumer<String> log) {
        this.log = log;
        this.installResult = null;
        this.name = null;
    }

    @Override
    public void displayResult(InstallResult installResult, String name) {
        this.log.accept(name + ": " + installResult.type().reason());
    }
}
