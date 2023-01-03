package org.hydev.mcpm.client.display.presenters;

import org.hydev.mcpm.client.commands.presenters.UninstallResultPresenter;
import org.hydev.mcpm.client.interaction.ILogger;
import org.hydev.mcpm.client.uninstall.UninstallResult;

/**
 * Implementation of UninstallResultPresenter
 */
public class UninstallPresenter implements UninstallResultPresenter {
    /**
     * Display one result
     *
     * @param name Name of the plugin
     * @param state State
     * @param log Logger object
     */
    private void display(String name, UninstallResult.State state, ILogger log) {
        var prefix = "[" + name + "] ";
        switch (state) {
            case NOT_FOUND -> log.print(prefix + "&cPlugin not found");
            case FAILED_TO_DELETE -> log.print(prefix + "&cFailed to delete plugin file");
            case SUCCESS -> log.print(prefix + "&aPlugin uninstalled successfully!");
            default -> log.print(state.name());
        }
    }

    @Override
    public void displayResult(String name, UninstallResult result, ILogger log) {
        display(name, result.state(), log);
        result.dependencies().forEach((depName, depState) -> display(depName, depState, log));
    }
}
