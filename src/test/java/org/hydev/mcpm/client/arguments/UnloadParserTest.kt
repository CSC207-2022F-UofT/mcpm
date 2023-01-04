package org.hydev.mcpm.client.arguments

import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.hydev.mcpm.client.arguments.mock.MockUnloadBoundary
import org.hydev.mcpm.client.arguments.parsers.UnloadParser
import org.hydev.mcpm.client.commands.controllers.UnloadController
import org.hydev.mcpm.client.interaction.NullLogger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Tests both the UnloadParser and UnloadController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
class UnloadParserTest
{
    private lateinit var unloader: MockUnloadBoundary
    private lateinit var controller: UnloadController
    private lateinit var args: ArgsParser

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    fun setup()
    {
        unloader = MockUnloadBoundary()
        controller = UnloadController(unloader)
        val parser = UnloadParser(controller)
        args = ArgsParser(listOf(parser))
    }

    /**
     * Tests if unload parser will correctly fail when no arguments are passed.
     */
    @Test
    fun testNoArguments() = runBlocking {
        val exception = assertThrows<ArgumentParserException> { args.parse(arrayOf("unload"), NullLogger()) }
        Assertions.assertEquals(exception.message, "too few arguments")
    }

    /**
     * Test that the `unload` boundary is correctly called with one plugin name.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testOnePlugin() = runBlocking {
        args.parse(arrayOf("unload", "myPlugin"), NullLogger())
        Assertions.assertEquals(HashSet(unloader.names), setOf("myPlugin"))
    }

    /**
     * Test that the `unload` boundary is correctly called when provided multiple names.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testManyPlugins() = runBlocking {
        args.parse(arrayOf("unload", "plugin1", "plugin2", "plugin3"), NullLogger())
        Assertions.assertEquals(HashSet(unloader.names), setOf("plugin1", "plugin2", "plugin3"))
    }

    /**
     * Test that the `unload` controller correctly calls the boundary with multiple names.
     */
    @Test
    fun testController()
    {
        controller.unload(listOf("plugin1", "plugin2", "plugin3"), NullLogger())
        Assertions.assertEquals(HashSet(unloader.names), setOf("plugin1", "plugin2", "plugin3"))
    }

    /**
     * Test that the `unload` boundary is still invoked with all plugin names even when one fails.
     */
    @Test
    fun testNotFound()
    {
        unloader.setThrowsNotFound(true)
        controller.unload(listOf("plugin1", "plugin2", "plugin3"), NullLogger())

        // Should still make all requests.
        Assertions.assertEquals(HashSet(unloader.names), setOf("plugin1", "plugin2", "plugin3"))
    }
}
