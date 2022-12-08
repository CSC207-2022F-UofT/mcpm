package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.commands.presenters.ImportPresenter;
import org.hydev.mcpm.client.export.ImportInput;
import org.hydev.mcpm.client.export.ImportPluginsBoundary;

import java.util.function.Consumer;

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
    public void importPlugins(ImportInput input, Consumer<String> log) {
        var result = boundary.importPlugins(input);
        presenter.present(result, log);
    }
}
