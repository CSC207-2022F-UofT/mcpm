package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockExportBoundary;
import org.hydev.mcpm.client.arguments.mock.MockInstallBoundary;
import org.hydev.mcpm.client.arguments.parsers.ExportPluginsParser;
import org.hydev.mcpm.client.commands.controllers.ExportPluginsController;
import org.hydev.mcpm.client.export.ExportPluginsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contains tests for testing the export controller and parser objects.
 * E.g. whether strings commands will result in correct inputs, call the right methods in the boundary, etc.
 */
public class ExportParserTest {
    private MockExportBoundary exporter;
    private ExportPluginsController controller;

    private ArgsParser args;


    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    public void setup() {
        exporter = new MockExportBoundary();
        controller = new ExportPluginsController(exporter);
        var parser = new ExportPluginsParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    /**
     * Tests if export parser will correctly fail when no arguments are passed.
     */
    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "export" }, log -> { })
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    /**
     * Tests whether an output stream is created when passed an outfile.
     */
    @Test
    @Tag("IntegrationTest")
    void testWithOutfile() throws ArgumentParserException, IOException {
        var file = File.createTempFile("myFile", ".json");

        // This does actually open a file stream, so it's hard to test.
        // Going to assume actually having ArgsParser creating the stream has no side effects.
        args.parse(new String[] { "export", file.getAbsolutePath() }, log -> { });

        var inputs = exporter.getInputs();
        assertEquals(inputs.size(), 1);

        var input = inputs.get(0);
        // Not going to bother checking stream, I don't trust it totally works here.
        assertTrue(input.cache());
    }

    /**
     * Tests whether no-cache is correctly set when the --cache option is provided.
     */
    @Test
    @Tag("IntegrationTest")
    void testWithNoCache() throws ArgumentParserException, IOException {
        var file = File.createTempFile("myFile", ".json");

        // This does actually open a file stream, so it's hard to test.
        // Going to assume actually having ArgsParser creating the stream has no side effects.
        args.parse(new String[] { "export", file.getAbsolutePath(), "--cache", "false" }, log -> { });

        var inputs = exporter.getInputs();
        assertEquals(inputs.size(), 1);

        var input = inputs.get(0);
        assertFalse(input.cache());
    }

    /**
     * Tests whether the controller will properly generate an input object when invoked.
     */
    @Test
    void testControllerWithRegularStream() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        controller.export(stream, true, log -> { });
        var inputs = exporter.getInputs();
        assertEquals(inputs.size(), 1);

        var input = inputs.get(0);
        assertEquals(input.out(), stream);
        assertTrue(input.cache());
    }

    /**
     * Tests whether a controller will still fail gracefully when passed a default result.
     */
    @Test
    void testControllerWithFailState() {
        exporter.setDefaultResult(ExportPluginsResult.State.FAILED_TO_FETCH_PLUGINS);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        controller.export(stream, true, log -> { });
        var inputs = exporter.getInputs();
        assertEquals(inputs.size(), 1);

        var input = inputs.get(0);
        // Should still receive a request. Not going to check log again since its presentation logic.
        assertEquals(input.out(), stream);
        assertTrue(input.cache());
    }
}
