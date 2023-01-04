package org.hydev.mcpm.client.display.presenters;

import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter;
import org.hydev.mcpm.client.installer.output.InstallResult;
import org.hydev.mcpm.client.interaction.ILogger;

import java.util.List;

/**
 * Implementation to the ResultPresenter, display the result of installation of plugins
 */
public class InstallPresenter implements InstallResultPresenter {

    @Override
    public void displayResult(List<InstallResult> results, ILogger log) {
        for (var r : results) {
            log.print("[" + r.name() + "] " + r.type().reason());
        }
    }
}
