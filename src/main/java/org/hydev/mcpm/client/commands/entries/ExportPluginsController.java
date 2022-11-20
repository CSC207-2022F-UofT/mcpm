package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.database.boundary.ExportPluginsBoundary;
import org.hydev.mcpm.client.database.inputs.ExportPluginsInput;

import java.util.function.Consumer;

/**
 * Controller for the export plugins use case.
 */
public class ExportPluginsController {
    private ExportPluginsBoundary boundary;

    public ExportPluginsController(ExportPluginsBoundary boundary) {
        this.boundary = boundary;
    }

    public void export(ExportPluginsInput input, Consumer<String> log) {
        var result = boundary.export(input);
        log.accept(String.format("Export status: %s\n", result.state().toString()));
    }
}
