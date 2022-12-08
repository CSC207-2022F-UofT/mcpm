package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.arguments.mock.MockExportBoundary;
import org.hydev.mcpm.client.arguments.parsers.ExportPluginsParser;
import org.hydev.mcpm.client.commands.controllers.ExportController;
import org.hydev.mcpm.client.export.ExportPluginsInput;
import org.hydev.mcpm.client.export.ExportPluginsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Contains tests for testing the export controller and parser objects.
 * E.g. whether strings commands will result in correct inputs, call the right methods in the boundary, etc.
 */
public class ExportParserTest {
    private MockExportBoundary exporter;
    private ExportController controller;

    private ArgsParser args;


    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    public void setup() {
        exporter = new MockExportBoundary();
        controller = new ExportController(exporter, (presenter, log) -> { });
        var parser = new ExportPluginsParser(controller);
        args = new ArgsParser(List.of(parser));
    }


    /**
     * Tests whether a controller will still fail gracefully when passed a default result.
     */
    @Test
    void testControllerWithFailState() {
        exporter.setDefaultResult(ExportPluginsResult.State.FAILED);

        controller.export(new ExportPluginsInput("literal", null), log -> { });
        var inputs = exporter.getInputs();
        assertEquals(inputs.size(), 1);

        var input = inputs.get(0);
        // Should still receive a request. Not going to check log again since it's presentation logic.
        assertEquals(input.type(), "literal");
        assertNull(input.out());
    }
}
