package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.arguments.parsers.CommandConfigurator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contains tests for testing the parser factory class.
 * Generally, we only want to make sure that we have entries for all the basic commands
 * that are required for our app to work.
 */
public class ParserFactoryTest {
    /**
     * Tests if ParserFactory will generate a list of plugins that has entries for all main commands.
     * Ex. export, list, search...
     * The use should be able to invoke these commands so it's important that we support this.
     */
    @Test
    @Tag("IntegrationTest")
    void testBasePlugins() {
        var interactors = new InteractorFactory(false);
        var controllers = new ControllerFactory(interactors);

        var parsers = ParserFactory.baseParsers(controllers);

        var names = parsers.stream()
            .map(CommandConfigurator::name)
            .collect(Collectors.toSet());

        var expected = Set.of(
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
        );

        assertTrue(names.containsAll(expected));
    }
}
