package org.hydev.mcpm.client.display.presenters;

import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter;
import org.hydev.mcpm.client.installer.InstallResult;

import java.util.List;
import java.util.function.Consumer;

/**
 * Implementation to the ResultPresenter, display the result of installation of plugins
 */
public class InstallPresenter implements InstallResultPresenter {

    @Override
    public void displayResult(List<InstallResult> results, Consumer<String> log) {
        for (var r : results) {
            log.accept("[" + r.name() + "] " + r.type().reason());
        }
    }
}
