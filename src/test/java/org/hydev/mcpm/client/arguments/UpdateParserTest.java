package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockUpdateBoundary;
import org.hydev.mcpm.client.arguments.parsers.UpdateParser;
import org.hydev.mcpm.client.commands.controllers.UpdateController;
import org.hydev.mcpm.client.database.SilentInstallPresenter;
import org.hydev.mcpm.client.database.SilentUpdatePresenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tests both the UpdateParser and UpdateController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
public class UpdateParserTest {
    private MockUpdateBoundary updater;
    private UpdateController controller;

    private ArgsParser args;

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    public void setup() {
        updater = new MockUpdateBoundary();
        controller = new UpdateController(updater);
        var parser = new UpdateParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    /**
     * Tests that the correct "update all" input is generated when the user does not pass a parameter.
     */
    @Test
    void testUpdateAll() throws ArgumentParserException {
        args.parse(new String[] { "update" }, log -> {});
        var inputs = updater.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertTrue(input.updateAll());
        assertFalse(input.load());
        assertFalse(input.noCache());
        assertTrue(input.pluginNames().isEmpty());
    }

    /**
     * Tests that the input object has noCache set when the no-cache parameter is provided.
     */
    @Test
    void testUpdateNoCache() throws ArgumentParserException {
        args.parse(new String[] { "update", "--no-cache" }, log -> {});
        var inputs = updater.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertTrue(input.updateAll());
        assertFalse(input.load());
        assertTrue(input.noCache());
        assertTrue(input.pluginNames().isEmpty());
    }

    /**
     * Tests that the input object has the load field set when the load parameter is provided.
     */
    @Test
    void testUpdateWithLoad() throws ArgumentParserException {
        args.parse(new String[] { "update", "--load" }, log -> {});
        var inputs = updater.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertTrue(input.updateAll());
        assertTrue(input.load());
        assertFalse(input.noCache());
        assertTrue(input.pluginNames().isEmpty());
    }

    /**
     * Tests that the plugin names list is properly set with the correct plugin name when a plugin name is provided.
     */
    @Test
    void testUpdateSingleName() throws ArgumentParserException {
        args.parse(new String[] { "update", "HelloWorld" }, log -> {});
        var inputs = updater.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertFalse(input.updateAll());
        assertFalse(input.load());
        assertFalse(input.noCache());
        assertEquals(new HashSet<>(input.pluginNames()), Set.of("HelloWorld"));
    }

    /**
     * Tests that all plugin names are provided in the input object when many are provided.
     */
    @Test
    void testUpdateManyNames() throws ArgumentParserException {
        args.parse(new String[] { "update", "TpProtect", "RealWorld" }, log -> {});
        var inputs = updater.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertFalse(input.updateAll());
        assertFalse(input.load());
        assertFalse(input.noCache());
        assertEquals(new HashSet<>(input.pluginNames()), Set.of("TpProtect", "RealWorld"));
    }

    /**
     * Tests that the correct input object is generated when controller is invoked.
     */
    @Test
    void testControllerInvocation() {
        var updatePresenter = new SilentUpdatePresenter();
        var installPresenter = new SilentInstallPresenter();

        controller.update(List.of("One", "Two"), true, false, updatePresenter);

        var inputs = updater.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertFalse(input.updateAll());
        assertTrue(input.load());
        assertFalse(input.noCache());
        assertEquals(new HashSet<>(input.pluginNames()), Set.of("One", "Two"));
    }
}
