package org.hydev.mcpm.client.arguments

import kotlinx.coroutines.runBlocking
import net.sourceforge.argparse4j.inf.ArgumentParserException
import net.sourceforge.argparse4j.inf.Namespace
import net.sourceforge.argparse4j.inf.Subparser
import org.hydev.mcpm.client.arguments.parsers.CommandParser
import org.hydev.mcpm.client.interaction.ILogger
import org.hydev.mcpm.client.interaction.StdLogger
import org.hydev.mcpm.utils.ColorLogger
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Tests for the ArgsParser class (whether it can handle parsing basic commands, etc).
 */
internal class ArgsParserTest
{
    internal class TestCommand : CommandParser
    {
        override val name = "test"
        override val description = "A testing command"

        override suspend fun run(details: Namespace, log: ILogger)
        {
            log.print(details.toString())
            assertEquals(details.getString("a"), "meow")
        }

        override fun configure(parser: Subparser)
        {
            parser.addArgument("a")
        }
    }

    /**
     * Tests whether the argument parser can parse basic strings for a test command.
     */
    @Test
    @Throws(ArgumentParserException::class)
    fun testParse(): Unit = runBlocking {
        val out: ILogger = StdLogger()
        val p = ArgsParser(listOf(TestCommand()))
        p.parse(arrayOf("test", "meow"), out)
        // Should print help
        p.parse(arrayOf(), out)
        // Should print test help
        p.parse(arrayOf("test", "-h"), out)
        assertThrows<AssertionError> { p.parse(arrayOf("test", "asd"), out) }
        ColorLogger.printc(p.help)
        assertEquals(p.rawSubparsers.size, 1)
    }

    /**
     * Tests whether the argument parser will fail on invalid invocations.
     */
    @Test
    fun testFail(): Unit = runBlocking {
        val out: ILogger = StdLogger()
        val p = ArgsParser(listOf(TestCommand()))
        assertThrows<ArgumentParserException> { p.parse(arrayOf("a"), out) }
    }
}
