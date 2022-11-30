package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.export.ExportPluginsBoundary;
import org.hydev.mcpm.client.export.ExportPluginsInput;

import java.util.function.Consumer;

/**
 * Controller for the export plugins use case.
 */
public class ExportPluginsController {
    private final ExportPluginsBoundary boundary;

    /**
     * Create a controller using the given boundary to process
     *
     * @param boundary the boundary to process the use case
     */
    public ExportPluginsController(ExportPluginsBoundary boundary) {
        this.boundary = boundary;
    }

    /**
     * Call the boundary to perform an export.
     *
     * @param input Input specifying the export parameters
     * @param log where to log the operation
     */
    public void export(ExportPluginsInput input, Consumer<String> log) {
        var result = boundary.export(input);
        log.accept(String.format("Export status: %s\n", result.state().toString()));
    }
}
