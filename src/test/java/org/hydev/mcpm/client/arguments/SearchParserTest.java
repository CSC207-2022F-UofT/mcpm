package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockSearchBoundary;
import org.hydev.mcpm.client.arguments.mock.MockSearchPresenter;
import org.hydev.mcpm.client.arguments.parsers.SearchParser;
import org.hydev.mcpm.client.commands.controllers.SearchPackagesController;
import org.hydev.mcpm.client.interaction.NullLogger;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests both the SearchParser and SearchController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
public class SearchParserTest {
    private MockSearchBoundary searcher;
    private SearchPackagesController controller;
    private ArgsParser args;

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    public void setup() {
        searcher = new MockSearchBoundary();
        controller = new SearchPackagesController(searcher);
        var parser = new SearchParser(controller, new MockSearchPresenter());
        args = new ArgsParser(List.of(parser));
    }

    /**
     * Tests if search parser will correctly fail when no arguments are passed.
     */
    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "search" }, new NullLogger())
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    /**
     * Tests that the search parser will correctly queue an object to search for a single term.
     */
    @Test
    void testSearchOneTerm() throws ArgumentParserException {
        args.parse(new String[] { "search", "test" }, new NullLogger());
        var inputs = searcher.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertFalse(input.noCache());
        assertEquals(input.type(), SearchPackagesType.BY_NAME);
        assertEquals(input.searchStr(), "test");
    }

    /**
     * Tests that the search parser will correctly queue an object to search for many terms (concat keywords).
     */
    @Test
    void testSearchManyTerms() throws ArgumentParserException {
        args.parse(new String[] { "search", "test", "two", "three" }, new NullLogger());
        var inputs = searcher.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertFalse(input.noCache());
        assertEquals(input.type(), SearchPackagesType.BY_NAME);
        assertEquals(input.searchStr(), "test two three");
    }

    /**
     * Tests that the search parser will set the noCache option when --no-cache is provided.
     */
    @Test
    void testSearchNoCache() throws ArgumentParserException {
        args.parse(new String[] { "search", "test", "two", "--no-cache" }, new NullLogger());
        var inputs = searcher.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertTrue(input.noCache());
        assertEquals(input.type(), SearchPackagesType.BY_NAME);
        assertEquals(input.searchStr(), "test two");
    }

    /**
     * Tests that the search parser will correctly set the search type to command when the --command option is provided.
     */
    @Test
    void testSearchByCommand() throws ArgumentParserException {
        args.parse(new String[] { "search", "--command", "hello" }, new NullLogger());
        var inputs = searcher.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertFalse(input.noCache());
        assertEquals(input.type(), SearchPackagesType.BY_COMMAND);
        assertEquals(input.searchStr(), "hello");
    }

    /**
     * Tests that the search parser will correctly set the search type to keyword when the --keyword option is provided.
     */
    @Test
    void testSearchByKeyword() throws ArgumentParserException {
        args.parse(new String[] { "search", "--keyword", "hello", "world" }, new NullLogger());
        var inputs = searcher.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertFalse(input.noCache());
        assertEquals(input.type(), SearchPackagesType.BY_KEYWORD);
        assertEquals(input.searchStr(), "hello world");
    }

    /**
     * Tests that the search controller will queue up a correct input object for two names.
     */
    @Test
    void testSearchController() {
        controller.searchPackages("name", List.of("pizza", "man"), false);
        var inputs = searcher.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertFalse(input.noCache());
        assertEquals(input.type(), SearchPackagesType.BY_NAME);
        assertEquals(input.searchStr(), "pizza man");
    }

    /**
     * Tests that the search controller will throw when passed an invalid type.
     */
    @Test
    void testSearchControllerInvalidType() {
        assertThrows(
            IllegalArgumentException.class,
            () -> controller.searchPackages("pizza", List.of("abc"), false)
        );
    }
}
