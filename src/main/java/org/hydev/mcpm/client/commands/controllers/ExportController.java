package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.commands.presenters.ExportPresenter;
import org.hydev.mcpm.client.export.ExportPluginsBoundary;
import org.hydev.mcpm.client.export.ExportPluginsInput;
import org.hydev.mcpm.client.interaction.ILogger;

/**
 * Controller for the export plugins use case.
 *
 * @param boundary The export implementation
 * @param presenter The presenter to show the result
 */
public record ExportController(
        ExportPluginsBoundary boundary,
        ExportPresenter presenter
) {

    /**
     * Call the boundary to perform an export.
     *
     * @param input Input specifying the export parameters
     * @param log where to log the operation
     */
    public void export(ExportPluginsInput input, ILogger log) {
        var result = boundary.export(input);
        presenter.present(result, log);
    }
}
