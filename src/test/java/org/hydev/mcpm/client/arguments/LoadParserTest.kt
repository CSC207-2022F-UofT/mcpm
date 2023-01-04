package org.hydev.mcpm.client.arguments

import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.hydev.mcpm.client.arguments.mock.MockLoadBoundary
import org.hydev.mcpm.client.arguments.parsers.LoadParser
import org.hydev.mcpm.client.commands.controllers.LoadController
import org.hydev.mcpm.client.interaction.NullLogger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Tests both the UnloadParser and UnloadController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
class LoadParserTest
{
    private lateinit var loader: MockLoadBoundary
    private lateinit var controller: LoadController
    private lateinit var args: ArgsParser

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    fun setup()
    {
        loader = MockLoadBoundary()
        controller = LoadController(loader)
        val parser = LoadParser(controller)
        args = ArgsParser(listOf(parser))
    }

    /**
     * Tests if load parser will correctly fail when no arguments are passed.
     */
    @Test
    fun testNoArguments() = runBlocking {
        val exception = assertThrows<ArgumentParserException>  { args.parse(arrayOf("load"), NullLogger()) }
        Assertions.assertEquals(exception.message, "too few arguments")
    }

    /**
     * Test that the `load` boundary is correctly called with one plugin name.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testOnePlugin() = runBlocking {
        args.parse(arrayOf("load", "myPlugin"), NullLogger())
        Assertions.assertEquals(HashSet(loader.names), setOf("myPlugin"))
    }

    /**
     * Test that the `reload` boundary is correctly called when provided multiple names.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testManyPlugins() = runBlocking {
        args.parse(arrayOf("load", "plugin1", "plugin2", "plugin3"), NullLogger())
        Assertions.assertEquals(HashSet(loader.names), setOf("plugin1", "plugin2", "plugin3"))
    }

    /**
     * Test that the `load` controller correctly calls the boundary with multiple names.
     */
    @Test
    fun testController()
    {
        controller.load(listOf("plugin1", "plugin2", "plugin3"), NullLogger())
        Assertions.assertEquals(HashSet(loader.names), setOf("plugin1", "plugin2", "plugin3"))
    }

    /**
     * Test that the `load` boundary is still invoked with all plugins even when one plugin is marked as not found.
     */
    @Test
    fun testNotFound()
    {
        loader.setThrowsNotFound(true)
        controller.load(listOf("plugin1", "plugin2", "plugin3"), NullLogger())

        // Should still make all requests.
        Assertions.assertEquals(HashSet(loader.names), setOf("plugin1", "plugin2", "plugin3"))
    }

    /**
     * Test that the `reload` boundary is still invoked with all plugins even when one plugin fails to load.
     */
    @Test
    fun testFailToLoad()
    {
        loader.setDefaultResult(false)
        controller.load(listOf("plugin1", "plugin2", "plugin3"), NullLogger())

        // Should still make all requests. Not a great other way to check without looking at presentation.
        Assertions.assertEquals(HashSet(loader.names), setOf("plugin1", "plugin2", "plugin3"))
    }
}
