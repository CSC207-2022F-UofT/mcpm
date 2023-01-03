package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockListBoundary;
import org.hydev.mcpm.client.arguments.parsers.ListParser;
import org.hydev.mcpm.client.commands.controllers.ListController;
import org.hydev.mcpm.client.interaction.NullLogger;
import org.hydev.mcpm.client.list.ListType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests both the ListParser and ListController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
public class ListParserTest {
    private MockListBoundary lister;
    private ListController controller;
    private ArgsParser args;

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    public void setup() {
        lister = new MockListBoundary();
        controller = new ListController(lister);
        var parser = new ListParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    /**
     * Tests whether the `list` parser will try to list all plugins when no input is provided.
     */
    @Test
    void testListDefault() throws ArgumentParserException {
        args.parse(new String[] { "list" }, new NullLogger());

        var types = lister.getTypes();
        assertEquals(types.size(), 1);
        assertEquals(types.get(0), ListType.ALL);
    }

    /**
     * Tests whether the `list` parser will try to list all plugins with the "all" option.
     */
    @Test
    void testListAll() throws ArgumentParserException {
        args.parse(new String[] { "list", "all" }, new NullLogger());

        var types = lister.getTypes();
        assertEquals(types.size(), 1);
        assertEquals(types.get(0), ListType.ALL);
    }

    /**
     * Tests whether the `list` parser will try to list all plugins with the "manual" option.
     */
    @Test
    void testListManual() throws ArgumentParserException {
        args.parse(new String[] { "list", "manual" }, new NullLogger());

        var types = lister.getTypes();
        assertEquals(types.size(), 1);
        assertEquals(types.get(0), ListType.MANUAL);
    }

    /**
     * Tests whether the `list` parser will try to list all plugins with the "automatic" option.
     */
    @Test
    void testListAutomatic() throws ArgumentParserException {
        args.parse(new String[] { "list", "automatic" }, new NullLogger());

        var types = lister.getTypes();
        assertEquals(types.size(), 1);
        assertEquals(types.get(0), ListType.AUTOMATIC);
    }

    /**
     * Tests whether the `list` parser will try to list all plugins with the "outdated" option.
     */
    @Test
    void testListOutdated() throws ArgumentParserException {
        args.parse(new String[] { "list", "outdated" }, new NullLogger());

        var types = lister.getTypes();
        assertEquals(types.size(), 1);
        assertEquals(types.get(0), ListType.OUTDATED);
    }

    /**
     * Tests whether the `list` parser will throw an error when provided an invalid option.
     */
    @Test
    void testListInvalid() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "list", "pizza" }, new NullLogger())
        );

        // Again, debatable if this is the best way to tell what kind of error is happening.
        var error = "argument type: invalid choice: 'pizza' (choose from {all,manual,automatic,outdated})";
        assertEquals(exception.getMessage(), error);
    }

    /**
     * Tests whether the `list` parser will throw an error when provided too many list options.
     */
    @Test
    void testTooManyArguments() {
        assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "list", "all", "outdated" }, new NullLogger())
        );
    }

    /**
     * Tests whether the `list` controller will correctly queue a input object when provided.
     */
    @Test
    void testController() {
        controller.listAll("manual", new NullLogger());

        var types = lister.getTypes();
        assertEquals(types.size(), 1);
        assertEquals(types.get(0), ListType.MANUAL);
    }

    /**
     * Tests whether the `list` controller will avoid queuing an object when provided an invalid input.
     */
    @Test
    void testControllerInvalid() {
        controller.listAll("pizza", new NullLogger());

        assertTrue(lister.getTypes().isEmpty());
    }
}
