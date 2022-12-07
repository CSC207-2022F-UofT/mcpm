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

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    public void setup() {
        uninstaller = new MockUninstallBoundary();
        controller = new UninstallController(uninstaller);
        var parser = new UninstallParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    /**
     * Tests if uninstall parser will correctly fail when no arguments are passed.
     */
    @Test
    public void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "uninstall" }, log -> { })
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    /**
     * Tests if uninstall parser will correctly make a request to remove one plugin when it is provided.
     */
    @Test
    public void testOnePlugin() throws ArgumentParserException {
        args.parse(new String[] { "uninstall", "myPlugin" }, log -> { });

        var inputs = uninstaller.getInputs();
        assertEquals(inputs.size(), 1);

        var input = inputs.get(0);
        assertEquals(input.name(), "myPlugin");
        assertTrue(input.recursive());
    }

    /**
     * Tests if uninstall parser will set the noRecursive variable when the --no-recursive option is provided.
     */
    @Test
    public void testWithRecursive() throws ArgumentParserException {
        args.parse(new String[] { "uninstall", "newPlugin", "--no-recursive" }, log -> { });

        var inputs = uninstaller.getInputs();
        assertEquals(inputs.size(), 1);

        var input = inputs.get(0);
        assertEquals(input.name(), "newPlugin");
        assertFalse(input.recursive());
    }

    /**
     * Tests if uninstall parser will fail gracefully when the uninstaller fails to delete a plugin.
     */
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

    /**
     * Tests if uninstall controller will correctly make a request to uninstall a plugin.
     */
    @Test
    public void testWithController() {
        controller.uninstall("my hello", true);

        var inputs = uninstaller.getInputs();
        assertEquals(inputs.size(), 1);

        // Should still put in a request.
        var input = inputs.get(0);
        assertEquals(input.name(), "my hello");
        assertTrue(input.recursive());
    }
}
