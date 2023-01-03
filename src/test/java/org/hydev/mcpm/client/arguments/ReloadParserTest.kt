package org.hydev.mcpm.client.arguments

import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.hydev.mcpm.client.arguments.mock.MockReloadBoundary
import org.hydev.mcpm.client.arguments.parsers.ReloadParser
import org.hydev.mcpm.client.commands.controllers.ReloadController
import org.hydev.mcpm.client.interaction.NullLogger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Tests both the ReloadParser and ReloadController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
class ReloadParserTest
{
    private lateinit var reloader: MockReloadBoundary
    private lateinit var controller: ReloadController
    private lateinit var args: ArgsParser

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    fun setup()
    {
        reloader = MockReloadBoundary()
        controller = ReloadController(reloader)
        val parser = ReloadParser(controller)
        args = ArgsParser(listOf(parser))
    }

    /**
     * Tests if reload parser will correctly fail when no arguments are passed.
     */
    @Test
    fun testNoArguments() = runBlocking {
        val exception = assertThrows<ArgumentParserException>  { args.parse(arrayOf("reload"), NullLogger()) }
        Assertions.assertEquals(exception.message, "too few arguments")
    }

    /**
     * Test that the `reload` boundary is correctly called with one plugin name.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testOnePlugin() = runBlocking {
        args.parse(arrayOf("reload", "myPlugin"), NullLogger())
        Assertions.assertEquals(HashSet(reloader.names), setOf("myPlugin"))
    }

    /**
     * Test that the `reload` boundary is correctly called when provided multiple names.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testManyPlugins() = runBlocking {
        args.parse(arrayOf("reload", "plugin1", "plugin2", "plugin3"), NullLogger())
        Assertions.assertEquals(HashSet(reloader.names), setOf("plugin1", "plugin2", "plugin3"))
    }

    /**
     * Test that the `reload` controller correctly calls the boundary with multiple names.
     */
    @Test
    fun testController()
    {
        controller.reload(listOf("plugin1", "plugin2", "plugin3"), NullLogger())
        Assertions.assertEquals(HashSet(reloader.names), setOf("plugin1", "plugin2", "plugin3"))
    }

    /**
     * Test that the `reload` boundary is still invoked with all plugins even when one plugin is marked as not found.
     */
    @Test
    fun testNotFound()
    {
        reloader.setThrowsNotFound(true)
        controller.reload(listOf("plugin1", "plugin2", "plugin3"), NullLogger())

        // All plugins should still be reloaded.
        Assertions.assertEquals(HashSet(reloader.names), setOf("plugin1", "plugin2", "plugin3"))
    }
}
