package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockInstallBoundary;
import org.hydev.mcpm.client.arguments.parsers.InstallParser;
import org.hydev.mcpm.client.commands.controllers.InstallController;
import org.hydev.mcpm.client.display.presenters.InstallPresenter;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests both the InstallParser and InstallController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
public class InstallParserTest {
    private MockInstallBoundary installer;
    private InstallController controller;

    private ArgsParser args;

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    public void setup() {
        installer = new MockInstallBoundary();
        var presenter = new InstallPresenter();
        controller = new InstallController(installer);
        var parser = new InstallParser(controller, presenter);
        args = new ArgsParser(List.of(parser));
    }

    void assertAcceptable(InstallInput input, String name, boolean load) {
        assertEquals(input.name(), name);
        assertEquals(input.type(), SearchPackagesType.BY_NAME);
        assertEquals(input.load(), load); // should we keep this?
        assertTrue(input.isManuallyInstalled());
    }

    /**
     * Tests if install parser will correctly fail when no arguments are passed.
     */
    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "install" }, log -> {})
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    /**
     * Tests whether the `install` parser will correctly invoke the boundary with one name.
     */
    @Test
    void testInstallSingleName() throws ArgumentParserException {

        args.parse(new String[] { "install", "JedCore" }, log -> {});
        var inputs = installer.getInputs();

        assertEquals(inputs.size(), 1);
        assertAcceptable(inputs.get(0), "JedCore", true);
    }

    /**
     * Tests whether the `install` parser will correctly set the noLoad parameter when the --no-load option is provided.
     */
    @Test
    void testInstallNoLoad() throws ArgumentParserException {
        args.parse(new String[] { "install", "ABC", "--no-load" }, log -> {});
        var inputs = installer.getInputs();

        assertEquals(inputs.size(), 1);
        assertAcceptable(inputs.get(0), "ABC", false);
    }

    /**
     * Tests whether the `install` controller will correctly invoke the boundary (with different package types).
     */
    @Test
    void testInstallController() {
        controller.install("MyPlugin", SearchPackagesType.BY_COMMAND, false);

        var inputs = installer.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertEquals(input.name(), "MyPlugin");
        assertEquals(input.type(), SearchPackagesType.BY_COMMAND);
        assertFalse(input.load());
        assertTrue(input.isManuallyInstalled());
    }
}
