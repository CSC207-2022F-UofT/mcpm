package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.arguments.mock.MockMirrorBoundary;
import org.hydev.mcpm.client.arguments.parsers.MirrorParser;
import org.hydev.mcpm.client.commands.controllers.MirrorController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

public class MirrorParserTest {
    private MockMirrorBoundary mirrors;

    private ArgsParser args;

    @BeforeEach
    public void setup() {
        var mirrorList = List.of(
            MockMirrorBoundary.mockMirror("mcpm.pizza.com"),
            MockMirrorBoundary.mockMirror("mcpm.sales.com"),
            MockMirrorBoundary.mockMirror("mcpm.something.com"),
            MockMirrorBoundary.mockMirror("mcpm.another.com")
        );

        mirrors = new MockMirrorBoundary(mirrorList);
        var controller = new MirrorController(mirrors);
        var parser = new MirrorParser(controller);
        args = new ArgsParser(List.of(parser));
    }

    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "mirror" }, log -> { })
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    @Test
    void testPing() throws ArgumentParserException {
        args.parse(new String[] { "mirror", "ping" }, log -> { });

        // Not going to bother to test log output, just expected behaviour for the MirrorBoundary.
        // Feel free to contribute something like InfoController's tests if you want.
        assertTrue(mirrors.getDidPingMirrors());
    }

    @Test
    void testPingThrowing() throws ArgumentParserException {
        mirrors.setThrowsIOException(true);

        args.parse(new String[] { "mirror", "ping" }, log -> { });

        mirrors.setThrowsIOException(false);

        // Not going to bother to test log output, just expected behaviour for the MirrorBoundary.
        // Feel free to contribute something like InfoController's tests if you want.

        // We're just looking for no extreme behaviour here... It should not reach the point where mirrors are pinged.
        assertFalse(mirrors.getDidUpdateMirrors());
        assertFalse(mirrors.getDidPingMirrors());
    }

    @Test
    void testPingRefreshing() throws ArgumentParserException {
        args.parse(new String[] { "mirror", "ping", "--refresh" }, log -> { });

        // We're just looking for no extreme behaviour here... It should not reach the point where mirrors are pinged.
        assertTrue(mirrors.getDidUpdateMirrors());
        assertTrue(mirrors.getDidPingMirrors());
    }

    @Test
    void testSelectNoArguments() throws ArgumentParserException, IOException {
        args.parse(new String[] { "mirror", "select" }, log -> { });

        // This is the default value for MockMirrorSelector.
        // I guess there's also no guarantee that selected mirror works?
        assertEquals(mirrors.getSelectedMirror().host(), "mcpm.pizza.com");
    }

    @Test
    void testSelectHost() throws ArgumentParserException, IOException {
        args.parse(new String[] { "mirror", "select", "mcpm.something.com" }, log -> { });

        assertEquals(mirrors.getSelectedMirror().host(), "mcpm.something.com");
    }

    @Test
    void testSelectMissing() throws ArgumentParserException, IOException {
        args.parse(new String[] { "mirror", "select", "mcpm.some.mirror.com" }, log -> { });

        // This is the default value for MockMirrorSelector.
        assertEquals(mirrors.getSelectedMirror().host(), "mcpm.pizza.com");
    }

    @Test
    void testSelectThrows() throws ArgumentParserException, IOException {
        mirrors.setThrowsIOException(true);

        args.parse(new String[] { "mirror", "select", "mcpm.something.com" }, log -> { });

        mirrors.setThrowsIOException(false);

        // This is the default value for MockMirrorSelector, again no change is expected.
        assertEquals(mirrors.getSelectedMirror().host(), "mcpm.pizza.com");
    }

    @Test
    void testInvalidOp() throws ArgumentParserException, IOException {
        mirrors.setThrowsIOException(true);

        args.parse(new String[] { "mirror", "select", "mcpm.something.com" }, log -> { });

        mirrors.setThrowsIOException(false);

        // This is the default value for MockMirrorSelector, again no change is expected.
        assertEquals(mirrors.getSelectedMirror().host(), "mcpm.pizza.com");
    }
}
