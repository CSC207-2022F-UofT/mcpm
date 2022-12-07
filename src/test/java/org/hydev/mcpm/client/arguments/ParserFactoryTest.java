package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.arguments.parsers.CommandConfigurator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserFactoryTest {
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
