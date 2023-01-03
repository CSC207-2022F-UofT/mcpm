package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.commands.presenters.ImportPresenter;
import org.hydev.mcpm.client.export.ImportInput;
import org.hydev.mcpm.client.export.ImportPluginsBoundary;
import org.hydev.mcpm.client.interaction.ILogger;

/**
 * Controller for the import use case.
 *
 * @param boundary The boundary used to import
 * @param presenter The presenter to display the result
 */
public record ImportController(
        ImportPluginsBoundary boundary,
        ImportPresenter presenter
) {

    /**
     * Import the plugins.
     *
     * @param input Specification of where to import from
     * @param log The log to write to
     */
    public void importPlugins(ImportInput input, ILogger log) {
        var result = boundary.importPlugins(input);
        presenter.present(result, log);
    }
}
