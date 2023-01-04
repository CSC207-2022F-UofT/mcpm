package org.hydev.mcpm.client.arguments

import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.hydev.mcpm.client.arguments.parsers.InfoParser
import org.hydev.mcpm.client.commands.controllers.InfoController
import org.hydev.mcpm.client.database.MockPluginTracker
import org.hydev.mcpm.client.database.PluginMockFactory
import org.hydev.mcpm.client.display.presenters.KvInfoPresenter
import org.hydev.mcpm.client.interaction.ILogger
import org.hydev.mcpm.client.interaction.NullLogger
import org.hydev.mcpm.client.loader.PluginNotFoundException
import org.hydev.mcpm.client.models.PluginCommand
import org.hydev.mcpm.utils.ColorLogger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.atomic.AtomicReference

/**
 * Contains tests for testing the info controller and parser objects.
 * E.g. whether strings commands will result in correct inputs, call the right methods in the boundary, etc.
 */
class InfoParserTest
{
    private lateinit var output: AtomicReference<String>
    private lateinit var log: ILogger
    private lateinit var controller: InfoController
    private lateinit var args: ArgsParser

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    fun setup()
    {
        output = AtomicReference("")
        log = object : ILogger
        {
            override suspend fun input() = ""
            override fun print(txt: String)
            {
                output.set(output.get() + txt)
            }
        }
        val command = PluginCommand(
            "desc", listOf("alias"), "perms", "usage  string"
        )
        val plugins = listOf(
            PluginMockFactory.meta("My Plugin", "v1.0", "a"),
            PluginMockFactory.meta(
                "My Plugin2", "v1.1", "bc", mapOf("comm" to command), listOf("Hello world")
            ),
            PluginMockFactory.meta("Plugin Plus", "v1.2", "def")
        )
        val tracker = MockPluginTracker(plugins)
        controller = InfoController(tracker)
        val parser = InfoParser(controller, KvInfoPresenter())
        args = ArgsParser(listOf(parser))
    }

    /**
     * Tests if info parser will correctly fail when no arguments are passed.
     */
    @Test
    fun testNoArguments() = runBlocking {
        val exception = assertThrows<ArgumentParserException> {
            args.parse(arrayOf("info"), NullLogger())
        }
        Assertions.assertEquals(exception.message, "too few arguments")
    }
    // Here we'll be interacting with log strings more since info only writes to log.
    // Not sure how we can do better than this, we will lock in InfoController into a format.
    // This would be moved to presenter when info is moved to presenter.
    /**
     * Tests whether the info parser will present the correct message when passed a plugin that does not exist.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testPluginAbsent() = runBlocking {
        args.parse(arrayOf("info", "what"), log)
        val expected = "Cannot find plugin 'what'"
        Assertions.assertEquals(ColorLogger.trimNoColor(output.get()), expected)
    }

    /**
     * Tests whether the info parser will print the correct plugin details for a small plugin.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testPluginDetails() = runBlocking {
        args.parse(arrayOf("info", "My Plugin"), log)
        val expected = """
Plugin Info:
> Name         : My Plugin
> Main         : org.My Plugin
> Version      : v1.0
> Description  : a
            """.trimIndent().trim { it <= ' ' } + "\n"
        Assertions.assertEquals(ColorLogger.trimNoColor(output.get()), expected)
    }

    /**
     * Tests whether the info parser will present the correct message when pass a plugin with non-null list/map values.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testPluginDetailsExtended() = runBlocking {
        args.parse(arrayOf("info", "My Plugin2"), log)
        val expected = """
Plugin Info:
> Name         : My Plugin2
> Main         : org.My Plugin2
> Version      : v1.1
> Description  : bc
> Authors      : Hello world
> Commands     : comm
            
            """.trimIndent().trim { it <= ' ' } + "\n"
        Assertions.assertEquals(expected, ColorLogger.trimNoColor(output.get()))
    }

    /**
     * Tests whether the info parser will present the correct message when invoked via controller.
     */
    @Test
    @Throws(PluginNotFoundException::class)
    fun testPluginDetailsDirectly()
    {
        val yml = controller.info("My Plugin2")
        Assertions.assertEquals(yml.name, "My Plugin2")
        Assertions.assertEquals(yml.main, "org.My Plugin2")
        Assertions.assertEquals(yml.version, "v1.1")
        Assertions.assertEquals(yml.description, "bc")
        Assertions.assertEquals(yml.firstAuthor, "Hello world")
        Assertions.assertTrue(yml.commands.containsKey("comm"))
    }
}
