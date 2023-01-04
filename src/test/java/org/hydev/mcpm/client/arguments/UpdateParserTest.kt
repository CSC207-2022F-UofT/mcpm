package org.hydev.mcpm.client.arguments

import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.hydev.mcpm.client.arguments.mock.MockUpdateBoundary
import org.hydev.mcpm.client.arguments.parsers.UpdateParser
import org.hydev.mcpm.client.commands.controllers.UpdateController
import org.hydev.mcpm.client.database.SilentUpdatePresenter
import org.hydev.mcpm.client.interaction.NullLogger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Tests both the UpdateParser and UpdateController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
class UpdateParserTest
{
    private lateinit var updater: MockUpdateBoundary
    private lateinit var controller: UpdateController
    private lateinit var args: ArgsParser

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    fun setup()
    {
        updater = MockUpdateBoundary()
        controller = UpdateController(updater)
        val parser = UpdateParser(controller)
        args = ArgsParser(listOf(parser))
    }

    /**
     * Tests that the correct "update all" input is generated when the user does not pass a parameter.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testUpdateAll() = runBlocking {
        args.parse(arrayOf("update"), NullLogger())
        val inputs = updater.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertTrue(input.updateAll())
        Assertions.assertFalse(input.load)
        Assertions.assertFalse(input.noCache)
        Assertions.assertTrue(input.pluginNames.isEmpty())
    }

    /**
     * Tests that the input object has noCache set when the no-cache parameter is provided.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testUpdateNoCache() = runBlocking {
        args.parse(arrayOf("update", "--no-cache"), NullLogger())
        val inputs = updater.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertTrue(input.updateAll())
        Assertions.assertFalse(input.load)
        Assertions.assertTrue(input.noCache)
        Assertions.assertTrue(input.pluginNames.isEmpty())
    }

    /**
     * Tests that the input object has the load field set when the load parameter is provided.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testUpdateWithLoad() = runBlocking {
        args.parse(arrayOf("update", "--load"), NullLogger())
        val inputs = updater.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertTrue(input.updateAll())
        Assertions.assertTrue(input.load)
        Assertions.assertFalse(input.noCache)
        Assertions.assertTrue(input.pluginNames.isEmpty())
    }

    /**
     * Tests that the plugin names list is properly set with the correct plugin name when a plugin name is provided.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testUpdateSingleName() = runBlocking {
        args.parse(arrayOf("update", "HelloWorld"), NullLogger())
        val inputs = updater.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertFalse(input.updateAll())
        Assertions.assertFalse(input.load)
        Assertions.assertFalse(input.noCache)
        Assertions.assertEquals(HashSet(input.pluginNames), setOf("HelloWorld"))
    }

    /**
     * Tests that all plugin names are provided in the input object when many are provided.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testUpdateManyNames() = runBlocking {
        args.parse(arrayOf("update", "TpProtect", "RealWorld"), NullLogger())
        val inputs = updater.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertFalse(input.updateAll())
        Assertions.assertFalse(input.load)
        Assertions.assertFalse(input.noCache)
        Assertions.assertEquals(HashSet(input.pluginNames), setOf("TpProtect", "RealWorld"))
    }

    /**
     * Tests that the correct input object is generated when controller is invoked.
     */
    @Test
    fun testControllerInvocation()
    {
        val updatePresenter = SilentUpdatePresenter()
        controller.update(listOf("One", "Two"), true, false, updatePresenter)
        val inputs = updater.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertFalse(input.updateAll())
        Assertions.assertTrue(input.load)
        Assertions.assertFalse(input.noCache)
        Assertions.assertEquals(HashSet(input.pluginNames), setOf("One", "Two"))
    }
}
