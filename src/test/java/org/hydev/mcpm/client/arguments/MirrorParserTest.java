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

/**
 * Contains tests for testing the mirror controller and parser objects.
 * E.g. whether strings commands will result in correct inputs, call the right methods in the boundary, etc.
 */
public class MirrorParserTest {
    private MockMirrorBoundary mirrors;

    private ArgsParser args;

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
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

    /**
     * Tests whether the mirror parser will throw when provided no arguments.
     */
    @Test
    void testNoArguments() {
        var exception = assertThrows(
            ArgumentParserException.class,
            () -> args.parse(new String[] { "mirror" }, log -> { })
        );

        assertEquals(exception.getMessage(), "too few arguments");
    }

    /**
     * Tests whether the mirror parser will actually invoke the ping method when passed the "ping" option.
     */
    @Test
    void testPing() throws ArgumentParserException {
        args.parse(new String[] { "mirror", "ping" }, log -> { });

        // Not going to bother to test log output, just expected behaviour for the MirrorBoundary.
        // Feel free to contribute something like InfoController's tests if you want.
        assertTrue(mirrors.getDidPingMirrors());
    }

    /**
     * Tests whether the mirror parser will avoid failure when the ping fails (throws an IOException).
     */
    @Test
    void testPingThrowing() throws ArgumentParserException {
        mirrors.setThrowsException(true);

        args.parse(new String[] { "mirror", "ping" }, log -> { });

        mirrors.setThrowsException(false);

        // Not going to bother to test log output, just expected behaviour for the MirrorBoundary.
        // Feel free to contribute something like InfoController's tests if you want.

        // We're just looking for no extreme behaviour here... It should not reach the point where mirrors are pinged.
        assertFalse(mirrors.getDidUpdateMirrors());
        assertFalse(mirrors.getDidPingMirrors());
    }

    /**
     * Tests whether the mirror parser will refresh plugins before pinging if provided the --refresh option.
     */
    @Test
    void testPingRefreshing() throws ArgumentParserException {
        args.parse(new String[] { "mirror", "ping", "--refresh" }, log -> { });

        // We're just looking for no extreme behaviour here... It should not reach the point where mirrors are pinged.
        assertTrue(mirrors.getDidUpdateMirrors());
        assertTrue(mirrors.getDidPingMirrors());
    }

    /**
     * Tests whether the mirror parser will not switch selection when no host is provided to "select".
     */
    @Test
    void testSelectNoArguments() throws ArgumentParserException, IOException {
        args.parse(new String[] { "mirror", "select" }, log -> { });

        // This is the default value for MockMirrorSelector.
        // I guess there's also no guarantee that selected mirror works?
        assertEquals(mirrors.getSelectedMirror().host(), "mcpm.pizza.com");
    }

    /**
     * Tests whether the mirror parser will switch the mirror selection with a valid host.
     */
    @Test
    void testSelectHost() throws ArgumentParserException, IOException {
        args.parse(new String[] { "mirror", "select", "mcpm.something.com" }, log -> { });

        assertEquals(mirrors.getSelectedMirror().host(), "mcpm.something.com");
    }

    /**
     * Tests whether the mirror parser will not switch selection when provided a host that does not exist.
     */
    @Test
    void testSelectMissing() throws ArgumentParserException, IOException {
        args.parse(new String[] { "mirror", "select", "mcpm.some.mirror.com" }, log -> { });

        // This is the default value for MockMirrorSelector.
        assertEquals(mirrors.getSelectedMirror().host(), "mcpm.pizza.com");
    }

    /**
     * Tests whether the mirror parser will not change the selected host when the mirrors fail to be acquired.
     */
    @Test
    void testSelectThrows() throws ArgumentParserException, IOException {
        mirrors.setThrowsException(true);

        args.parse(new String[] { "mirror", "select", "mcpm.something.com" }, log -> { });

        mirrors.setThrowsException(false);

        // This is the default value for MockMirrorSelector, again no change is expected.
        assertEquals(mirrors.getSelectedMirror().host(), "mcpm.pizza.com");
    }
}
