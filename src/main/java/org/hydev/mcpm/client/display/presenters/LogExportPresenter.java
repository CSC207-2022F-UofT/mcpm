package org.hydev.mcpm.client.display.presenters;

import org.hydev.mcpm.client.commands.presenters.ExportPresenter;
import org.hydev.mcpm.client.export.ExportPluginsResult;

import java.util.function.Consumer;

/**
 * ExportPresenter that presents to a log.
 *
 * @param log The log to present to
 */
public record LogExportPresenter(Consumer<String> log) implements ExportPresenter {

    private String getColor(ExportPluginsResult exportPluginsResult) {
        return switch (exportPluginsResult.state()) {
            case SUCCESS -> "&a";
            case FAILED_TO_FETCH_PLUGINS -> "&c";
        };
    }

    private String getMessage(ExportPluginsResult exportPluginsResult) {
        return switch ((exportPluginsResult.state())) {
            case SUCCESS -> exportPluginsResult.export();
            case FAILED_TO_FETCH_PLUGINS -> "Failed to fetch plugins";
        };
    }


    @Override
    public void present(ExportPluginsResult exportPluginsResult) {
        log.accept(getColor(exportPluginsResult) + getMessage(exportPluginsResult));
    }
}
