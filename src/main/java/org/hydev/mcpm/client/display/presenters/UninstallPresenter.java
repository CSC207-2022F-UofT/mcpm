package org.hydev.mcpm.client.display.presenters;

import org.hydev.mcpm.client.commands.presenters.UninstallResultPresenter;
import org.hydev.mcpm.client.uninstall.UninstallResult;

import java.util.function.Consumer;

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
    private void display(String name, UninstallResult.State state, Consumer<String> log) {
        var prefix = "[" + name + "] ";
        switch (state) {
            case NOT_FOUND -> log.accept(prefix + "&cPlugin not found");
            case FAILED_TO_DELETE -> log.accept(prefix + "&cFailed to delete plugin file");
            case SUCCESS -> log.accept(prefix + "&aPlugin uninstalled successfully!");
            default -> log.accept(state.name());
        }
    }

    @Override
    public void displayResult(String name, UninstallResult result, Consumer<String> log) {
        display(name, result.state(), log);
        result.dependencies().forEach((depName, depState) -> display(depName, depState, log));
    }
}
