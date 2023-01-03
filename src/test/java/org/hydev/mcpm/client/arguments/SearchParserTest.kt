package org.hydev.mcpm.client.arguments

import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.hydev.mcpm.client.arguments.mock.MockSearchBoundary
import org.hydev.mcpm.client.arguments.mock.MockSearchPresenter
import org.hydev.mcpm.client.arguments.parsers.SearchParser
import org.hydev.mcpm.client.commands.controllers.SearchPackagesController
import org.hydev.mcpm.client.interaction.NullLogger
import org.hydev.mcpm.client.search.SearchPackagesType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Tests both the SearchParser and SearchController classes (since they are invoked in a similar way).
 * Since these classes require a similar setup, I've moved them together into this class.
 */
class SearchParserTest
{
    private lateinit var searcher: MockSearchBoundary
    private lateinit var controller: SearchPackagesController
    private lateinit var args: ArgsParser

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    fun setup()
    {
        searcher = MockSearchBoundary()
        controller = SearchPackagesController(searcher)
        val parser = SearchParser(controller, MockSearchPresenter())
        args = ArgsParser(listOf(parser))
    }

    /**
     * Tests if search parser will correctly fail when no arguments are passed.
     */
    @Test
    fun testNoArguments() = runBlocking {
        val exception = assertThrows<ArgumentParserException>  { args.parse(arrayOf("search"), NullLogger()) }
        Assertions.assertEquals(exception.message, "too few arguments")
    }

    /**
     * Tests that the search parser will correctly queue an object to search for a single term.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testSearchOneTerm() = runBlocking {
        args.parse(arrayOf("search", "test"), NullLogger())
        val inputs = searcher.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertFalse(input.noCache)
        Assertions.assertEquals(input.type, SearchPackagesType.BY_NAME)
        Assertions.assertEquals(input.searchStr, "test")
    }

    /**
     * Tests that the search parser will correctly queue an object to search for many terms (concat keywords).
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testSearchManyTerms() = runBlocking {
        args.parse(arrayOf("search", "test", "two", "three"), NullLogger())
        val inputs = searcher.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertFalse(input.noCache)
        Assertions.assertEquals(input.type, SearchPackagesType.BY_NAME)
        Assertions.assertEquals(input.searchStr, "test two three")
    }

    /**
     * Tests that the search parser will set the noCache option when --no-cache is provided.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testSearchNoCache() = runBlocking {
        args.parse(arrayOf("search", "test", "two", "--no-cache"), NullLogger())
        val inputs = searcher.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertTrue(input.noCache)
        Assertions.assertEquals(input.type, SearchPackagesType.BY_NAME)
        Assertions.assertEquals(input.searchStr, "test two")
    }

    /**
     * Tests that the search parser will correctly set the search type to command when the --command option is provided.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testSearchByCommand() = runBlocking {
        args.parse(arrayOf("search", "--command", "hello"), NullLogger())
        val inputs = searcher.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertFalse(input.noCache)
        Assertions.assertEquals(input.type, SearchPackagesType.BY_COMMAND)
        Assertions.assertEquals(input.searchStr, "hello")
    }

    /**
     * Tests that the search parser will correctly set the search type to keyword when the --keyword option is provided.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testSearchByKeyword() = runBlocking {
        args.parse(arrayOf("search", "--keyword", "hello", "world"), NullLogger())
        val inputs = searcher.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertFalse(input.noCache)
        Assertions.assertEquals(input.type, SearchPackagesType.BY_KEYWORD)
        Assertions.assertEquals(input.searchStr, "hello world")
    }

    /**
     * Tests that the search controller will queue up a correct input object for two names.
     */
    @Test
    fun testSearchController()
    {
        controller.searchPackages("name", listOf("pizza", "man"), false)
        val inputs = searcher.inputs
        Assertions.assertEquals(inputs.size, 1)
        val input = inputs[0]
        Assertions.assertFalse(input.noCache)
        Assertions.assertEquals(input.type, SearchPackagesType.BY_NAME)
        Assertions.assertEquals(input.searchStr, "pizza man")
    }

    /**
     * Tests that the search controller will throw when passed an invalid type.
     */
    @Test
    fun testSearchControllerInvalidType()
    {
        Assertions.assertThrows(
            IllegalArgumentException::class.java
        ) { controller.searchPackages("pizza", listOf("abc"), false) }
    }
}
