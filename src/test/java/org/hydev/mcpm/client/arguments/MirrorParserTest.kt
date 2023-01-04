package org.hydev.mcpm.client.arguments

import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.hydev.mcpm.client.arguments.mock.MockMirrorBoundary
import org.hydev.mcpm.client.arguments.parsers.MirrorParser
import org.hydev.mcpm.client.commands.controllers.MirrorController
import org.hydev.mcpm.client.interaction.NullLogger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

/**
 * Contains tests for testing the mirror controller and parser objects.
 * E.g. whether strings commands will result in correct inputs, call the right methods in the boundary, etc.
 */
class MirrorParserTest
{
    private lateinit var mirrors: MockMirrorBoundary
    private lateinit var args: ArgsParser

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    fun setup()
    {
        val mirrorList = listOf(
            MockMirrorBoundary.mockMirror("mcpm.pizza.com"),
            MockMirrorBoundary.mockMirror("mcpm.sales.com"),
            MockMirrorBoundary.mockMirror("mcpm.something.com"),
            MockMirrorBoundary.mockMirror("mcpm.another.com")
        )
        mirrors = MockMirrorBoundary(mirrorList)
        val controller = MirrorController(mirrors)
        val parser = MirrorParser(controller)
        args = ArgsParser(listOf(parser))
    }

    /**
     * Tests whether the mirror parser will throw when provided no arguments.
     */
    @Test
    fun testNoArguments() = runBlocking {
        val exception = assertThrows<ArgumentParserException>  { args.parse(arrayOf("mirror"), NullLogger()) }
        Assertions.assertEquals(exception.message, "too few arguments")
    }

    /**
     * Tests whether the mirror parser will actually invoke the ping method when passed the "ping" option.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testPing() = runBlocking {
        args.parse(arrayOf("mirror", "ping"), NullLogger())

        // Not going to bother to test log output, just expected behaviour for the MirrorBoundary.
        // Feel free to contribute something like InfoController's tests if you want.
        Assertions.assertTrue(mirrors.didPingMirrors)
    }

    /**
     * Tests whether the mirror parser will avoid failure when the ping fails (throws an IOException).
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testPingThrowing() = runBlocking {
        mirrors.setThrowsException(true)
        args.parse(arrayOf("mirror", "ping"), NullLogger())
        mirrors.setThrowsException(false)

        // Not going to bother to test log output, just expected behaviour for the MirrorBoundary.
        // Feel free to contribute something like InfoController's tests if you want.

        // We're just looking for no extreme behaviour here... It should not reach the point where mirrors are pinged.
        Assertions.assertFalse(mirrors.didUpdateMirrors)
        Assertions.assertFalse(mirrors.didPingMirrors)
    }

    /**
     * Tests whether the mirror parser will refresh plugins before pinging if provided the --refresh option.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testPingRefreshing() = runBlocking {
        args.parse(arrayOf("mirror", "ping", "--refresh"), NullLogger())

        // We're just looking for no extreme behaviour here... It should not reach the point where mirrors are pinged.
        Assertions.assertTrue(mirrors.didUpdateMirrors)
        Assertions.assertTrue(mirrors.didPingMirrors)
    }

    /**
     * Tests whether the mirror parser will not switch selection when no host is provided to "select".
     */
    @Test
    @Throws(ArgumentParserException::class, IOException::class)
    fun testSelectNoArguments() = runBlocking {
        args.parse(arrayOf("mirror", "select", "mcpm.pizza.com"), NullLogger())

        // This is the default value for MockMirrorSelector.
        // I guess there's also no guarantee that selected mirror works?
        Assertions.assertEquals(mirrors.selectedMirror.host, "mcpm.pizza.com")
    }

    /**
     * Tests whether the mirror parser will switch the mirror selection with a valid host.
     */
    @Test
    @Throws(ArgumentParserException::class, IOException::class)
    fun testSelectHost() = runBlocking {
        args.parse(arrayOf("mirror", "select", "mcpm.something.com"), NullLogger())
        Assertions.assertEquals(mirrors.selectedMirror.host, "mcpm.something.com")
    }

    /**
     * Tests whether the mirror parser will not switch selection when provided a host that does not exist.
     */
    @Test
    @Throws(ArgumentParserException::class, IOException::class)
    fun testSelectMissing() = runBlocking {
        args.parse(arrayOf("mirror", "select", "mcpm.some.mirror.com"), NullLogger())

        // This is the default value for MockMirrorSelector.
        Assertions.assertEquals(mirrors.selectedMirror.host, "mcpm.pizza.com")
    }

    /**
     * Tests whether the mirror parser will not change the selected host when the mirrors fail to be acquired.
     */
    @Test
    @Throws(ArgumentParserException::class, IOException::class)
    fun testSelectThrows() = runBlocking {
        mirrors.setThrowsException(true)
        args.parse(arrayOf("mirror", "select", "mcpm.something.com"), NullLogger())
        mirrors.setThrowsException(false)

        // This is the default value for MockMirrorSelector, again no change is expected.
        Assertions.assertEquals(mirrors.selectedMirror.host, "mcpm.pizza.com")
    }
}
