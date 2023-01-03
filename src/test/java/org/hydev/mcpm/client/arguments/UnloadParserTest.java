package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockUnloadBoundary;
import org.hydev.mcpm.client.arguments.parsers.UnloadParser;
import org.hydev.mcpm.client.commands.controllers.UnloadController;
import org.hydev.mcpm.client.interaction.NullLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests both the UnloadParser and UnloadController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
public class UnloadParserTest {
    private MockUnloadBoundary unloader;
    private UnloadController controller;

    private ArgsParser args;

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    public void setup() {
        unloader = new MockUnloadBoundary();
        controller = new UnloadController(unloader);
        var parser = new UnloadParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    /**
     * Tests if unload parser will correctly fail when no arguments are passed.
     */
    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "unload" }, new NullLogger())
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    /**
     * Test that the `unload` boundary is correctly called with one plugin name.
     */
    @Test
    void testOnePlugin() throws ArgumentParserException {
        args.parse(new String[] { "unload", "myPlugin" }, new NullLogger());

        assertEquals(new HashSet<>(unloader.getNames()), Set.of("myPlugin"));
    }

    /**
     * Test that the `unload` boundary is correctly called when provided multiple names.
     */
    @Test
    void testManyPlugins() throws ArgumentParserException {
        args.parse(new String[] { "unload", "plugin1", "plugin2", "plugin3" }, new NullLogger());

        assertEquals(new HashSet<>(unloader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }

    /**
     * Test that the `unload` controller correctly calls the boundary with multiple names.
     */
    @Test
    void testController() {
        controller.unload(List.of("plugin1", "plugin2", "plugin3"), new NullLogger());

        assertEquals(new HashSet<>(unloader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }

    /**
     * Test that the `unload` boundary is still invoked with all plugin names even when one fails.
     */
    @Test
    void testNotFound() {
        unloader.setThrowsNotFound(true);

        controller.unload(List.of("plugin1", "plugin2", "plugin3"), new NullLogger());

        // Should still make all requests.
        assertEquals(new HashSet<>(unloader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }
}
