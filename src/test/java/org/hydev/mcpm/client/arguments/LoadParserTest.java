package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockLoadBoundary;
import org.hydev.mcpm.client.arguments.parsers.LoadParser;
import org.hydev.mcpm.client.commands.controllers.LoadController;
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
public class LoadParserTest {
    private MockLoadBoundary loader;
    private LoadController controller;

    private ArgsParser args;

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    public void setup() {
        loader = new MockLoadBoundary();
        controller = new LoadController(loader);
        var parser = new LoadParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    /**
     * Tests if load parser will correctly fail when no arguments are passed.
     */
    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "load" }, log -> { })
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    /**
     * Test that the `load` boundary is correctly called with one plugin name.
     */
    @Test
    void testOnePlugin() throws ArgumentParserException {
        args.parse(new String[] { "load", "myPlugin" }, log -> { });

        assertEquals(new HashSet<>(loader.getNames()), Set.of("myPlugin"));
    }

    /**
     * Test that the `reload` boundary is correctly called when provided multiple names.
     */
    @Test
    void testManyPlugins() throws ArgumentParserException {
        args.parse(new String[] { "load", "plugin1", "plugin2", "plugin3" }, log -> { });

        assertEquals(new HashSet<>(loader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }

    /**
     * Test that the `load` controller correctly calls the boundary with multiple names.
     */
    @Test
    void testController() {
        controller.load(List.of("plugin1", "plugin2", "plugin3"), log -> { });

        assertEquals(new HashSet<>(loader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }

    /**
     * Test that the `load` boundary is still invoked with all plugins even when one plugin is marked as not found.
     */
    @Test
    void testNotFound() {
        loader.setThrowsNotFound(true);

        controller.load(List.of("plugin1", "plugin2", "plugin3"), log -> { });

        // Should still make all requests.
        assertEquals(new HashSet<>(loader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }

    /**
     * Test that the `reload` boundary is still invoked with all plugins even when one plugin fails to load.
     */
    @Test
    void testFailToLoad() {
        loader.setDefaultResult(false);

        controller.load(List.of("plugin1", "plugin2", "plugin3"), log -> { });

        // Should still make all requests. Not a great other way to check without looking at presentation.
        assertEquals(new HashSet<>(loader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }
}
