package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockListBoundary;
import org.hydev.mcpm.client.arguments.parsers.ListParser;
import org.hydev.mcpm.client.commands.controllers.ListController;
import org.hydev.mcpm.client.list.ListType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests both the ListParser and ListController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
public class ListParserTest {
    private MockListBoundary lister;
    private ListController controller;
    private ArgsParser args;

    @BeforeEach
    public void setup() {
        lister = new MockListBoundary();
        controller = new ListController(lister);
        var parser = new ListParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    @Test
    void testListDefault() throws ArgumentParserException {
        args.parse(new String[] { "list" }, log -> { });

        var types = lister.getTypes();
        assertEquals(types.size(), 1);
        assertEquals(types.get(0), ListType.ALL);
    }

    @Test
    void testListAll() throws ArgumentParserException {
        args.parse(new String[] { "list", "all" }, log -> { });

        var types = lister.getTypes();
        assertEquals(types.size(), 1);
        assertEquals(types.get(0), ListType.ALL);
    }

    @Test
    void testListManual() throws ArgumentParserException {
        args.parse(new String[] { "list", "manual" }, log -> { });

        var types = lister.getTypes();
        assertEquals(types.size(), 1);
        assertEquals(types.get(0), ListType.MANUAL);
    }

    @Test
    void testListAutomatic() throws ArgumentParserException {
        args.parse(new String[] { "list", "automatic" }, log -> { });

        var types = lister.getTypes();
        assertEquals(types.size(), 1);
        assertEquals(types.get(0), ListType.AUTOMATIC);
    }

    @Test
    void testListOutdated() throws ArgumentParserException {
        args.parse(new String[] { "list", "outdated" }, log -> { });

        var types = lister.getTypes();
        assertEquals(types.size(), 1);
        assertEquals(types.get(0), ListType.OUTDATED);
    }

    @Test
    void testListInvalid() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "list", "pizza" }, log -> { })
        );

        // Again, debatable if this is the best way to tell what kind of error is happening.
        var error = "argument type: invalid choice: 'pizza' (choose from {all,manual,automatic,outdated})";
        assertEquals(exception.getMessage(), error);
    }

    @Test
    void testTooManyArguments() {
        assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "list", "all", "outdated" }, log -> { })
        );
    }

    @Test
    void testController() {
        controller.listAll("manual", log -> { });

        var types = lister.getTypes();
        assertEquals(types.size(), 1);
        assertEquals(types.get(0), ListType.MANUAL);
    }


    @Test
    void testControllerInvalid() {
        controller.listAll("pizza", log -> { });

        assertTrue(lister.getTypes().isEmpty());
    }
}
