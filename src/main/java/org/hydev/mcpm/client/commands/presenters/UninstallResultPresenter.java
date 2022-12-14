package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.uninstall.UninstallResult;

import java.util.function.Consumer;

/**
 * Interface for presenting uninstall result
 */
public interface UninstallResultPresenter {
    /**
     * Display uninstall result
     *
     * @param result Result
     * @param log Logger
     */
    void displayResult(String name, UninstallResult result, Consumer<String> log);
}
