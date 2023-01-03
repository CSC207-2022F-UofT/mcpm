package org.hydev.mcpm.client.display.presenters;

import org.hydev.mcpm.client.commands.presenters.ImportPresenter;
import org.hydev.mcpm.client.export.ImportResult;
import org.hydev.mcpm.client.interaction.ILogger;

/**
 * ImportPresenter that writes to a log
 */
public class LogImportPresenter implements ImportPresenter {

    private String getColor(ImportResult result) {
        return switch (result.state()) {
            case SUCCESS -> "&a";
            case PARTIAL_SUCCESS -> "&6";
            case FAILURE -> "&c";
            case IMPORT_ERROR -> "&4";
        };
    }

    private String getMessage(ImportResult result) {
        return switch (result.state()) {
            case SUCCESS -> "All plugins installed";
            case PARTIAL_SUCCESS -> "Some plugins failed to install:\n"
                    + String.join("\n", result.nonInstalledPlugins);
            case FAILURE -> "All plugins failed to install";
            case IMPORT_ERROR -> "Import failed. " + result.error;
        };
    }

    @Override
    public void present(ImportResult result, ILogger log) {
        log.print(getColor(result) + getMessage(result));
    }
}
