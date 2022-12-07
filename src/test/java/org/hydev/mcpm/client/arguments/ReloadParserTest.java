package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockReloadBoundary;
import org.hydev.mcpm.client.arguments.mock.MockUnloadBoundary;
import org.hydev.mcpm.client.arguments.parsers.ReloadParser;
import org.hydev.mcpm.client.arguments.parsers.UnloadParser;
import org.hydev.mcpm.client.commands.controllers.ReloadController;
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
public class ReloadParserTest {
    private MockReloadBoundary reloader;
    private ReloadController controller;

    private ArgsParser args;

    @BeforeEach
    public void setup() {
        reloader = new MockReloadBoundary();
        controller = new ReloadController(reloader);
        var parser = new ReloadParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    // Similar set of tests to unloader and loader.
    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "reload" }, log -> { })
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    @Test
    void testOnePlugin() throws ArgumentParserException {
        args.parse(new String[] { "reload", "myPlugin" }, log -> { });

        assertEquals(new HashSet<>(reloader.getNames()), Set.of("myPlugin"));
    }

    @Test
    void testManyPlugins() throws ArgumentParserException {
        args.parse(new String[] { "reload", "plugin1", "plugin2", "plugin3" }, log -> { });

        assertEquals(new HashSet<>(reloader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }

    @Test
    void testController() {
        controller.reload(List.of("plugin1", "plugin2", "plugin3"), log -> { });

        assertEquals(new HashSet<>(reloader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }


    @Test
    void testNotFound() {
        reloader.setThrowsNotFound(true);

        controller.reload(List.of("plugin1", "plugin2", "plugin3"), log -> { });

        // All plugins should still be reloaded.
        assertEquals(new HashSet<>(reloader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }
}
