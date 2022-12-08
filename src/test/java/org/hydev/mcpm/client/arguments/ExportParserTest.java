package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.arguments.mock.MockExportBoundary;
import org.hydev.mcpm.client.commands.controllers.ExportController;
import org.hydev.mcpm.client.export.ExportPluginsInput;
import org.hydev.mcpm.client.export.ExportPluginsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Contains tests for testing the export controller and parser objects.
 * E.g. whether strings commands will result in correct inputs, call the right methods in the boundary, etc.
 */
public class ExportParserTest {
    private MockExportBoundary exporter;
    private ExportController controller;

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    public void setup() {
        exporter = new MockExportBoundary();
        controller = new ExportController(exporter, (presenter, log) -> { });
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
