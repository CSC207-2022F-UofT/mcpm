package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter;
import org.hydev.mcpm.client.installer.InstallResult;

/**
 * Mock Install Presenter that does nothing on invocation.
 */
public class SilentInstallPresenter implements InstallResultPresenter {
    @Override
    public void displayResult(InstallResult installResult, String name) {
        /* do nothing */
    }
}