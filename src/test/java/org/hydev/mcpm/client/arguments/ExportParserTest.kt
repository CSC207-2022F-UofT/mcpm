package org.hydev.mcpm.client.arguments

import org.hydev.mcpm.client.arguments.mock.MockExportBoundary
import org.hydev.mcpm.client.commands.controllers.ExportController
import org.hydev.mcpm.client.export.ExportPluginsInput
import org.hydev.mcpm.client.export.ExportPluginsResult
import org.hydev.mcpm.client.interaction.ILogger
import org.hydev.mcpm.client.interaction.NullLogger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Contains tests for testing the export controller and parser objects.
 * E.g. whether strings commands will result in correct inputs, call the right methods in the boundary, etc.
 */
class ExportParserTest
{
    private lateinit var exporter: MockExportBoundary
    private lateinit var controller: ExportController

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    fun setup()
    {
        exporter = MockExportBoundary()
        controller = ExportController(exporter) { presenter: ExportPluginsResult?, log: ILogger? -> }
    }

    /**
     * Tests whether a controller will still fail gracefully when passed a default result.
     */
    @Test
    fun testControllerWithFailState()
    {
        exporter.setDefaultResult(ExportPluginsResult.State.FAILED)
        controller.export(ExportPluginsInput("literal", null), NullLogger())
        val inputs = exporter.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        // Should still receive a request. Not going to check log again since it's presentation logic.
        Assertions.assertEquals(input.type, "literal")
        Assertions.assertNull(input.out)
    }
}
