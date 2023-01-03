package org.hydev.mcpm.client.arguments

import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.hydev.mcpm.client.arguments.mock.MockListBoundary
import org.hydev.mcpm.client.arguments.parsers.ListParser
import org.hydev.mcpm.client.commands.controllers.ListController
import org.hydev.mcpm.client.interaction.NullLogger
import org.hydev.mcpm.client.list.ListType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Tests both the ListParser and ListController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
class ListParserTest
{
    private lateinit var lister: MockListBoundary
    private lateinit var controller: ListController
    private lateinit var args: ArgsParser

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    fun setup()
    {
        lister = MockListBoundary()
        controller = ListController(lister)
        val parser = ListParser(controller)
        args = ArgsParser(listOf(parser))
    }

    /**
     * Tests whether the `list` parser will try to list all plugins when no input is provided.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testListDefault() = runBlocking {
        args.parse(arrayOf("list"), NullLogger())
        val types = lister.types
        Assertions.assertEquals(types.size, 1)
        Assertions.assertEquals(types[0], ListType.ALL)
    }

    /**
     * Tests whether the `list` parser will try to list all plugins with the "all" option.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testListAll() = runBlocking {
        args.parse(arrayOf("list", "all"), NullLogger())
        val types = lister.types
        Assertions.assertEquals(types.size, 1)
        Assertions.assertEquals(types[0], ListType.ALL)
    }

    /**
     * Tests whether the `list` parser will try to list all plugins with the "manual" option.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testListManual() = runBlocking {
        args.parse(arrayOf("list", "manual"), NullLogger())
        val types = lister.types
        Assertions.assertEquals(types.size, 1)
        Assertions.assertEquals(types[0], ListType.MANUAL)
    }

    /**
     * Tests whether the `list` parser will try to list all plugins with the "automatic" option.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testListAutomatic() = runBlocking {
        args.parse(arrayOf("list", "automatic"), NullLogger())
        val types = lister.types
        Assertions.assertEquals(types.size, 1)
        Assertions.assertEquals(types[0], ListType.AUTOMATIC)
    }

    /**
     * Tests whether the `list` parser will try to list all plugins with the "outdated" option.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testListOutdated() = runBlocking {
        args.parse(arrayOf("list", "outdated"), NullLogger())
        val types = lister.types
        Assertions.assertEquals(types.size, 1)
        Assertions.assertEquals(types[0], ListType.OUTDATED)
    }

    /**
     * Tests whether the `list` parser will throw an error when provided an invalid option.
     */
    @Test
    fun testListInvalid() = runBlocking {
        val exception = assertThrows<ArgumentParserException> { args.parse(arrayOf("list", "pizza"), NullLogger()) }

        // Again, debatable if this is the best way to tell what kind of error is happening.
        val error = "argument type: invalid choice: 'pizza' (choose from {all,manual,automatic,outdated})"
        Assertions.assertEquals(exception.message, error)
    }

    /**
     * Tests whether the `list` parser will throw an error when provided too many list options.
     */
    @Test
    fun testTooManyArguments(): Unit = runBlocking {
        assertThrows<ArgumentParserException> { args.parse(arrayOf("list", "all", "outdated"), NullLogger()) }
    }

    /**
     * Tests whether the `list` controller will correctly queue a input object when provided.
     */
    @Test
    fun testController() = runBlocking {
        controller.listAll("manual", NullLogger())
        val types = lister.types
        Assertions.assertEquals(types.size, 1)
        Assertions.assertEquals(types[0], ListType.MANUAL)
    }

    /**
     * Tests whether the `list` controller will avoid queuing an object when provided an invalid input.
     */
    @Test
    fun testControllerInvalid() = runBlocking {
        controller.listAll("pizza", NullLogger())
        Assertions.assertTrue(lister.types.isEmpty())
    }
}
