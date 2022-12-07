package org.hydev.mcpm.client.arguments;

import org.hydev.mcpm.client.arguments.parsers.CommandConfigurator;
import org.hydev.mcpm.client.arguments.parsers.ExportPluginsParser;
import org.hydev.mcpm.client.arguments.parsers.InfoParser;
import org.hydev.mcpm.client.arguments.parsers.InstallParser;
import org.hydev.mcpm.client.arguments.parsers.ListParser;
import org.hydev.mcpm.client.arguments.parsers.MirrorParser;
import org.hydev.mcpm.client.arguments.parsers.PageParser;
import org.hydev.mcpm.client.arguments.parsers.RefreshParser;
import org.hydev.mcpm.client.arguments.parsers.SearchParser;
import org.hydev.mcpm.client.arguments.parsers.UninstallParser;
import org.hydev.mcpm.client.arguments.parsers.UpdateParser;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.naming.ldap.Control;
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
