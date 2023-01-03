package org.hydev.mcpm.client.arguments;


import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockMirrorBoundary;
import org.hydev.mcpm.client.arguments.mock.MockRefreshFetcher;
import org.hydev.mcpm.client.arguments.parsers.RefreshParser;
import org.hydev.mcpm.client.commands.controllers.RefreshController;
import org.hydev.mcpm.client.database.fetcher.QuietFetcherListener;
import org.hydev.mcpm.client.interaction.NullLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contains tests for testing the refresh controller and parser objects.
 * E.g. whether strings commands will result in correct inputs, call the right methods in the boundary, etc.
 */
public class RefreshParserTest {
    private MockRefreshFetcher fetcher;
    private RefreshController controller;
    private ArgsParser args;

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    public void setup() {
        fetcher = new MockRefreshFetcher();
        var mirrors = new MockMirrorBoundary(List.of(
            MockMirrorBoundary.mockMirror("mcpm.pizza.com"),
            MockMirrorBoundary.mockMirror("mcpm.another.com")
        ));
        controller = new RefreshController(fetcher, new QuietFetcherListener(), mirrors);
        var parser = new RefreshParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    /**
     * Tests that the refresh command actually makes a request for the database.
     */
    @Test
    void testFetchDatabase() throws ArgumentParserException {
        args.parse(new String[] { "refresh" }, new NullLogger());

        assertTrue(fetcher.getFetched());
    }

    /**
     * Tests that the refresh command fails gracefully when the database is failed to be acquired.
     */
    @Test
    void testFailedToFetch() throws ArgumentParserException {
        fetcher.setDefaultResult(null);

        args.parse(new String[] { "refresh" }, new NullLogger());

        // We won't test for anything fancy here.
        // It should still fetch and pass, but I'm not going to bother uhh...
        // I'm not going to bother checking log. This method should really return a result.
        assertTrue(fetcher.getFetched());
    }

    /**
     * Tests that the refresh controller will directly make a request for the database.
     */
    @Test
    void testController() throws IOException {
        controller.refresh();

        assertTrue(fetcher.getFetched());
    }
}
