package org.hydev.mcpm.client.display.presenters;

import org.hydev.mcpm.client.commands.presenters.ExportPresenter;
import org.hydev.mcpm.client.export.ExportPluginsResult;

import java.util.function.Consumer;

/**
 * ExportPresenter that presents to a log.
 */
public class LogExportPresenter implements ExportPresenter {

    private String getColor(ExportPluginsResult result) {
        return switch (result.state()) {
            case SUCCESS -> "&a";
            case FAILED -> "&c";
        };
    }

    private String getMessage(ExportPluginsResult result) {
        return switch ((result.state())) {
            case SUCCESS -> "Exported to " + result.export();
            case FAILED -> result.error();
        };
    }


    @Override
    public void present(ExportPluginsResult exportPluginsResult, Consumer<String> log) {
        log.accept(getColor(exportPluginsResult) + getMessage(exportPluginsResult));
    }
}
