package org.hydev.mcpm.client.arguments

import org.hydev.mcpm.client.arguments.parsers.CommandConfigurator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.util.stream.Collectors

/**
 * Contains tests for testing the parser factory class.
 * Generally, we only want to make sure that we have entries for all the basic commands
 * that are required for our app to work.
 */
class ParserFactoryTest
{
    /**
     * Tests if ParserFactory will generate a list of plugins that has entries for all main commands.
     * Ex. export, list, search...
     * The use should be able to invoke these commands so it's important that we support this.
     */
    @Test
    @Tag("IntegrationTest")
    fun testBasePlugins()
    {
        val interactors = InteractorFactory(false)
        val controllers = ControllerFactory(interactors)
        val parsers = ParserFactory.baseParsers(controllers)
        val names = parsers.stream()
            .map(CommandConfigurator::name)
            .collect(Collectors.toSet())
        val expected = setOf(
            "export",
            "list",
            "search",
            "mirror",
            "info",
            "install",
            "refresh",
            "page",
            "uninstall",
            "update"
        )
        Assertions.assertTrue(names.containsAll(expected))
    }
}
