package org.hydev.mcpm.client.arguments;


import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockMirrorBoundary;
import org.hydev.mcpm.client.arguments.mock.MockRefreshFetcher;
import org.hydev.mcpm.client.arguments.parsers.RefreshParser;
import org.hydev.mcpm.client.commands.controllers.RefreshController;
import org.hydev.mcpm.client.database.fetcher.QuietFetcherListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RefreshParserTest {
    private MockRefreshFetcher fetcher;
    private RefreshController controller;
    private ArgsParser args;

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

    @Test
    void testFetchDatabase() throws ArgumentParserException {
        args.parse(new String[] { "refresh" }, log -> { });

        assertTrue(fetcher.getFetched());
    }

    @Test
    void testFailedToFetch() throws ArgumentParserException {
        fetcher.setDefaultResult(null);

        args.parse(new String[] { "refresh" }, log -> { });

        // We won't test for anything fancy here.
        // It should still fetch and pass, but I'm not going to bother uhh...
        // I'm not going to bother checking log. This method should really return a result.
        assertTrue(fetcher.getFetched());
    }


    @Test
    void testController() throws IOException {
        controller.refresh();

        assertTrue(fetcher.getFetched());
    }
}
