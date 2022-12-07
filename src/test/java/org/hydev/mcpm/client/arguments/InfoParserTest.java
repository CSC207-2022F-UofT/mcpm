package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.parsers.InfoParser;
import org.hydev.mcpm.client.commands.controllers.InfoController;
import org.hydev.mcpm.client.database.MockPluginTracker;
import org.hydev.mcpm.client.database.PluginMockFactory;
import org.hydev.mcpm.client.models.PluginCommand;
import org.hydev.mcpm.utils.ColorLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InfoParserTest {
    private AtomicReference<String> output;
    private Consumer<String> log;
    private MockPluginTracker tracker;
    private InfoController controller;

    private ArgsParser args;

    @BeforeEach
    public void setup() {
        output = new AtomicReference<>("");
        log = text -> output.set(output.get() + text);

        var command = new PluginCommand(
            "desc", List.of("alias"), "perms", "usage  string"
        );

        var plugins = List.of(
            PluginMockFactory.meta("My Plugin", "v1.0", "a"),
            PluginMockFactory.meta("My Plugin2", "v1.1", "bc", Map.of(
                "comm", command
            ), List.of("Hello world")),
            PluginMockFactory.meta("Plugin Plus", "v1.2", "def")
        );

        tracker = new MockPluginTracker(plugins);
        controller = new InfoController(tracker);
        var parser = new InfoParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "info" }, log -> { })
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    // Here we'll be interacting with log strings more since info only writes to log.
    // Not sure how we can do better than this, we're gonna lock in InfoController into a format.
    // This would be moved to presenter when info is moved to presenter.

    @Test
    void testPluginAbsent() throws ArgumentParserException {
        args.parse(new String[] { "info", "what" }, log);

        var expected = "Cannot find plugin 'what'";
        assertEquals(ColorLogger.trimNoColor(output.get()), expected);
    }

    @Test
    void testPluginDetails() throws ArgumentParserException {
        args.parse(new String[] { "info", "My Plugin" }, log);

        var expected = """
Plugin Info:
> Name         : My Plugin
> Main         : org.My Plugin
> Version      : v1.0
> Description  : a
            """.trim() + "\n";
        assertEquals(ColorLogger.trimNoColor(output.get()), expected);
    }

    @Test
    void testPluginDetailsExtended() throws ArgumentParserException {
        args.parse(new String[] { "info", "My Plugin2" }, log);

        var expected = """
Plugin Info:
> Name         : My Plugin2
> Main         : org.My Plugin2
> Version      : v1.1
> Description  : bc
> Authors      : Hello world
> Commands     : comm
            """.trim() + "\n";
        assertEquals(ColorLogger.trimNoColor(output.get()), expected);
    }
}
