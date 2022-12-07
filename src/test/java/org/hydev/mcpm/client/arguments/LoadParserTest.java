package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockLoadBoundary;
import org.hydev.mcpm.client.arguments.mock.MockUnloadBoundary;
import org.hydev.mcpm.client.arguments.parsers.LoadParser;
import org.hydev.mcpm.client.arguments.parsers.UnloadParser;
import org.hydev.mcpm.client.commands.controllers.LoadController;
import org.hydev.mcpm.client.commands.controllers.UnloadController;
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

    @BeforeEach
    public void setup() {
        loader = new MockLoadBoundary();
        controller = new LoadController(loader);
        var parser = new LoadParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    // Similar set of tests to unloader and reloader.
    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "load" }, log -> { })
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    @Test
    void testOnePlugin() throws ArgumentParserException {
        args.parse(new String[] { "load", "myPlugin" }, log -> { });

        assertEquals(new HashSet<>(loader.getNames()), Set.of("myPlugin"));
    }

    @Test
    void testManyPlugins() throws ArgumentParserException {
        args.parse(new String[] { "load", "plugin1", "plugin2", "plugin3" }, log -> { });

        assertEquals(new HashSet<>(loader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }

    @Test
    void testController() {
        controller.load(List.of("plugin1", "plugin2", "plugin3"), log -> { });

        assertEquals(new HashSet<>(loader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }

    @Test
    void testNotFound() {
        loader.setThrowsNotFound(true);

        controller.load(List.of("plugin1", "plugin2", "plugin3"), log -> { });

        // Should still make all requests.
        assertEquals(new HashSet<>(loader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }

    @Test
    void testFailToLoad() {
        loader.setDefaultResult(false);

        controller.load(List.of("plugin1", "plugin2", "plugin3"), log -> { });

        // Should still make all requests. Not a great other way to check without looking at presentation.
        assertEquals(new HashSet<>(loader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }
}
