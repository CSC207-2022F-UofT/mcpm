package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockUninstallBoundary;
import org.hydev.mcpm.client.arguments.parsers.UninstallParser;
import org.hydev.mcpm.client.commands.controllers.UninstallController;
import org.hydev.mcpm.client.uninstall.UninstallResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests both the UninstallParser and UninstallController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
public class UninstallParserTest {
    private MockUninstallBoundary uninstaller;
    private UninstallController controller;
    private ArgsParser args;

    @BeforeEach
    public void setup() {
        uninstaller = new MockUninstallBoundary();
        controller = new UninstallController(uninstaller);
        var parser = new UninstallParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    @Test
    public void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "uninstall" }, log -> { })
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    @Test
    public void testOnePlugin() throws ArgumentParserException {
        args.parse(new String[] { "uninstall", "myPlugin" }, log -> { });

        var inputs = uninstaller.getInputs();
        assertEquals(inputs.size(), 1);

        var input = inputs.get(0);
        assertEquals(input.name(), "myPlugin");
        assertTrue(input.recursive());
    }

    @Test
    public void testWithRecursive() throws ArgumentParserException {
        args.parse(new String[] { "uninstall", "newPlugin", "--no-recursive" }, log -> { });

        var inputs = uninstaller.getInputs();
        assertEquals(inputs.size(), 1);

        var input = inputs.get(0);
        assertEquals(input.name(), "newPlugin");
        assertFalse(input.recursive());
    }

    @Test
    public void testWithFailResult() throws ArgumentParserException {
        uninstaller.setDefaultState(UninstallResult.State.FAILED_TO_DELETE);

        args.parse(new String[] { "uninstall", "my hello" }, log -> { });
        // Should still pass.

        var inputs = uninstaller.getInputs();
        assertEquals(inputs.size(), 1);

        // Should still put in a request.
        var input = inputs.get(0);
        assertEquals(input.name(), "my hello");
        assertTrue(input.recursive());
    }
}
