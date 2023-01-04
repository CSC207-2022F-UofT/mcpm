package org.hydev.mcpm.client.arguments

import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.hydev.mcpm.client.arguments.mock.MockUninstallBoundary
import org.hydev.mcpm.client.arguments.parsers.UninstallParser
import org.hydev.mcpm.client.commands.controllers.UninstallController
import org.hydev.mcpm.client.display.presenters.UninstallPresenter
import org.hydev.mcpm.client.interaction.NullLogger
import org.hydev.mcpm.client.uninstall.UninstallResult
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Tests both the UninstallParser and UninstallController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
class UninstallParserTest
{
    private lateinit var uninstaller: MockUninstallBoundary
    private lateinit var controller: UninstallController
    private lateinit var args: ArgsParser

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    fun setup()
    {
        uninstaller = MockUninstallBoundary()
        controller = UninstallController(uninstaller)
        val parser = UninstallParser(controller, UninstallPresenter())
        args = ArgsParser(listOf(parser))
    }

    /**
     * Tests if uninstall parser will correctly fail when no arguments are passed.
     */
    @Test
    fun testNoArguments() = runBlocking {
        val exception = assertThrows<ArgumentParserException> { args.parse(arrayOf("uninstall"), NullLogger()) }
        Assertions.assertEquals(exception.message, "too few arguments")
    }

    /**
     * Tests if uninstall parser will correctly make a request to remove one plugin when it is provided.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testOnePlugin() = runBlocking {
        args.parse(arrayOf("uninstall", "myPlugin"), NullLogger())
        val inputs = uninstaller.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertEquals(input.name, "myPlugin")
        Assertions.assertTrue(input.recursive)
    }

    /**
     * Tests if uninstall parser will set the noRecursive variable when the --no-recursive option is provided.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testWithRecursive() = runBlocking {
        args.parse(arrayOf("uninstall", "newPlugin", "--no-recursive"), NullLogger())
        val inputs = uninstaller.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertEquals(input.name, "newPlugin")
        Assertions.assertFalse(input.recursive)
    }

    /**
     * Tests if uninstall parser will fail gracefully when the uninstaller fails to delete a plugin.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testWithFailResult() = runBlocking {
        uninstaller.setDefaultState(UninstallResult.State.FAILED_TO_DELETE)
        args.parse(arrayOf("uninstall", "my hello"), NullLogger())
        // Should still pass.
        val inputs = uninstaller.inputs
        Assertions.assertEquals(inputs.size, 1)

        // Should still put in a request.
        val input = inputs[0]
        Assertions.assertEquals(input.name, "my hello")
        Assertions.assertTrue(input.recursive)
    }

    /**
     * Tests if uninstall controller will correctly make a request to uninstall a plugin.
     */
    @Test
    fun testWithController()
    {
        controller.uninstall("my hello", true)
        val inputs = uninstaller.inputs
        Assertions.assertEquals(inputs.size, 1)

        // Should still put in a request.
        val input = inputs[0]
        Assertions.assertEquals(input.name, "my hello")
        Assertions.assertTrue(input.recursive)
    }
}
