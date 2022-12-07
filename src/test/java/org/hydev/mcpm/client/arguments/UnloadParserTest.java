package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockUnloadBoundary;
import org.hydev.mcpm.client.arguments.parsers.UnloadParser;
import org.hydev.mcpm.client.commands.controllers.UnloadController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tests both the UnloadParser and UnloadController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
public class UnloadParserTest {
    private MockUnloadBoundary unloader;
    private UnloadController controller;

    private ArgsParser args;

    @BeforeEach
    public void setup() {
        unloader = new MockUnloadBoundary();
        controller = new UnloadController(unloader);
        var parser = new UnloadParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "unload" }, log -> { })
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    @Test
    void testOnePlugin() throws ArgumentParserException {
        args.parse(new String[] { "unload", "myPlugin" }, log -> { });

        assertEquals(new HashSet<>(unloader.getNames()), Set.of("myPlugin"));
    }

    @Test
    void testManyPlugins() throws ArgumentParserException {
        args.parse(new String[] { "unload", "plugin1", "plugin2", "plugin3" }, log -> { });

        assertEquals(new HashSet<>(unloader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }

    @Test
    void testController() {
        controller.unload(List.of("plugin1", "plugin2", "plugin3"), log -> { });

        assertEquals(new HashSet<>(unloader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }

    @Test
    void testNotFound() {
        unloader.setThrowsNotFound(true);

        controller.unload(List.of("plugin1", "plugin2", "plugin3"), log -> { });

        // Should still make all requests.
        assertEquals(new HashSet<>(unloader.getNames()), Set.of("plugin1", "plugin2", "plugin3"));
    }
}
