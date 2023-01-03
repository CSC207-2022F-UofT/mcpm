package org.hydev.mcpm.client.arguments;

import kotlin.coroutines.Continuation;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.parsers.InfoParser;
import org.hydev.mcpm.client.commands.controllers.InfoController;
import org.hydev.mcpm.client.database.MockPluginTracker;
import org.hydev.mcpm.client.database.PluginMockFactory;
import org.hydev.mcpm.client.display.presenters.KvInfoPresenter;
import org.hydev.mcpm.client.interaction.ILogger;
import org.hydev.mcpm.client.interaction.NullLogger;
import org.hydev.mcpm.client.loader.PluginNotFoundException;
import org.hydev.mcpm.client.models.PluginCommand;
import org.hydev.mcpm.utils.ColorLogger;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains tests for testing the info controller and parser objects.
 * E.g. whether strings commands will result in correct inputs, call the right methods in the boundary, etc.
 */
public class InfoParserTest {
    private AtomicReference<String> output;
    private ILogger log;
    private InfoController controller;

    private ArgsParser args;

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    public void setup() {
        output = new AtomicReference<>("");
        log = new ILogger() {
            @Override
            public Object input(@NotNull Continuation<? super String> completion)
            {
                return null;
            }

            @Override
            public void print(@NotNull String txt)
            {
                output.set(output.get() + txt);
            }
        };

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

        var tracker = new MockPluginTracker(plugins);
        controller = new InfoController(tracker);
        var parser = new InfoParser(controller, new KvInfoPresenter());
        args = new ArgsParser(List.of(parser));
    }


    /**
     * Tests if info parser will correctly fail when no arguments are passed.
     */
    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "info" }, new NullLogger())
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    // Here we'll be interacting with log strings more since info only writes to log.
    // Not sure how we can do better than this, we will lock in InfoController into a format.
    // This would be moved to presenter when info is moved to presenter.

    /**
     * Tests whether the info parser will present the correct message when passed a plugin that does not exist.
     */
    @Test
    void testPluginAbsent() throws ArgumentParserException {
        args.parse(new String[] { "info", "what" }, log);

        var expected = "Cannot find plugin 'what'";
        assertEquals(ColorLogger.trimNoColor(output.get()), expected);
    }

    /**
     * Tests whether the info parser will print the correct plugin details for a small plugin.
     */
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

    /**
     * Tests whether the info parser will present the correct message when pass a plugin with non-null list/map values.
     */
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
        assertEquals(expected, ColorLogger.trimNoColor(output.get()));
    }

    /**
     * Tests whether the info parser will present the correct message when invoked via controller.
     */
    @Test
    void testPluginDetailsDirectly() throws PluginNotFoundException
    {
        var yml = controller.info("My Plugin2");
        assertEquals(yml.name(), "My Plugin2");
        assertEquals(yml.main(), "org.My Plugin2");
        assertEquals(yml.version(), "v1.1");
        assertEquals(yml.description(), "bc");
        assertEquals(yml.getFirstAuthor(), "Hello world");
        assertTrue(yml.commands().containsKey("comm"));
    }
}
