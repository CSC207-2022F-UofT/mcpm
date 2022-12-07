package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter;
import org.hydev.mcpm.client.installer.output.InstallResult;

import java.util.List;
import java.util.function.Consumer;

/**
 * Mock Install Presenter that does nothing on invocation.
 */
public class SilentInstallPresenter implements InstallResultPresenter {
    @Override
    public void displayResult(List<InstallResult> results, Consumer<String> log) {
        return;
    }
}
