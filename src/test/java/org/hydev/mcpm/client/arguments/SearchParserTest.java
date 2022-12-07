package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockSearchBoundary;
import org.hydev.mcpm.client.arguments.mock.MockSearchPresenter;
import org.hydev.mcpm.client.arguments.parsers.SearchParser;
import org.hydev.mcpm.client.commands.controllers.SearchPackagesController;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Tests both the SearchParser and SearchController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
public class SearchParserTest {
    private MockSearchBoundary searcher;
    private SearchPackagesController controller;
    private ArgsParser args;

    @BeforeEach
    public void setup() {
        searcher = new MockSearchBoundary();
        controller = new SearchPackagesController(searcher);
        var parser = new SearchParser(controller, new MockSearchPresenter());
        args = new ArgsParser(List.of(parser));
    }

    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "search" }, log -> {})
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    @Test
    void testSearchOneTerm() throws ArgumentParserException {
        args.parse(new String[] { "search", "test" }, log -> {});
        var inputs = searcher.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertFalse(input.noCache());
        assertEquals(input.type(), SearchPackagesType.BY_NAME);
        assertEquals(input.searchStr(), "test");
    }

    @Test
    void testSearchManyTerms() throws ArgumentParserException {
        args.parse(new String[] { "search", "test", "two", "three" }, log -> {});
        var inputs = searcher.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertFalse(input.noCache());
        assertEquals(input.type(), SearchPackagesType.BY_NAME);
        assertEquals(input.searchStr(), "test two three");
    }

    @Test
    void testSearchNoCache() throws ArgumentParserException {
        args.parse(new String[] { "search", "test", "two", "--no-cache" }, log -> {});
        var inputs = searcher.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertTrue(input.noCache());
        assertEquals(input.type(), SearchPackagesType.BY_NAME);
        assertEquals(input.searchStr(), "test two");
    }

    @Test
    void testSearchByCommand() throws ArgumentParserException {
        args.parse(new String[] { "search", "--command", "hello" }, log -> {});
        var inputs = searcher.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertFalse(input.noCache());
        assertEquals(input.type(), SearchPackagesType.BY_COMMAND);
        assertEquals(input.searchStr(), "hello");
    }

    @Test
    void testSearchByKeyword() throws ArgumentParserException {
        args.parse(new String[] { "search", "--keyword", "hello", "world" }, log -> {});
        var inputs = searcher.getInputs();

        assertEquals(inputs.size(), 1);
        var input = inputs.get(0);

        assertFalse(input.noCache());
        assertEquals(input.type(), SearchPackagesType.BY_KEYWORD);
        assertEquals(input.searchStr(), "hello world");
    }

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

    @Test
    void testSearchControllerInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.searchPackages("pizza", List.of("abc"), false);
        });
    }
}
