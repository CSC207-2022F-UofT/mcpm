package org.hydev.mcpm.client.arguments

import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.hydev.mcpm.client.arguments.mock.MockMirrorBoundary
import org.hydev.mcpm.client.arguments.mock.MockRefreshFetcher
import org.hydev.mcpm.client.arguments.parsers.RefreshParser
import org.hydev.mcpm.client.commands.controllers.RefreshController
import org.hydev.mcpm.client.database.fetcher.QuietFetcherListener
import org.hydev.mcpm.client.interaction.NullLogger
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException

/**
 * Contains tests for testing the refresh controller and parser objects.
 * E.g. whether strings commands will result in correct inputs, call the right methods in the boundary, etc.
 */
class RefreshParserTest
{
    private lateinit var fetcher: MockRefreshFetcher
    private lateinit var controller: RefreshController
    private lateinit var args: ArgsParser

    /**
     * Initializes the various fields (controllers, etc.) before a test starts.
     */
    @BeforeEach
    fun setup()
    {
        fetcher = MockRefreshFetcher()
        val mirrors = MockMirrorBoundary(
            listOf(
                MockMirrorBoundary.mockMirror("mcpm.pizza.com"),
                MockMirrorBoundary.mockMirror("mcpm.another.com")
            )
        )
        controller = RefreshController(fetcher, QuietFetcherListener(), mirrors)
        val parser = RefreshParser(controller)
        args = ArgsParser(listOf(parser))
    }

    /**
     * Tests that the refresh command actually makes a request for the database.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testFetchDatabase() = runBlocking {
        args.parse(arrayOf("refresh"), NullLogger())
        Assertions.assertTrue(fetcher.fetched)
    }

    /**
     * Tests that the refresh command fails gracefully when the database is failed to be acquired.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testFailedToFetch() = runBlocking {
        fetcher.setDefaultResult(null)
        args.parse(arrayOf("refresh"), NullLogger())

        // We won't test for anything fancy here.
        // It should still fetch and pass, but I'm not going to bother uhh...
        // I'm not going to bother checking log. This method should really return a result.
        Assertions.assertTrue(fetcher.fetched)
    }

    /**
     * Tests that the refresh controller will directly make a request for the database.
     */
    @Test
    @Throws(IOException::class)
    fun testController()
    {
        controller.refresh()
        Assertions.assertTrue(fetcher.fetched)
    }
}
