package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.ExportController;
import org.hydev.mcpm.client.export.ExportPluginsInput;
import org.hydev.mcpm.client.interaction.ILogger;

/**
 * Parser for the ExportPluginsBoundary interface.
 */
public record ExportPluginsParser(ExportController controller) implements CommandParser
{
    @Override
    public String name()
    {
        return "export";
    }

    @Override
    public String description()
    {
        return "Export plugin configuration";
    }

    @Override
    public void configure(Subparser parser)
    {
        parser.addArgument("type").nargs("?").choices("pastebin", "file", "literal")
                .setDefault("pastebin") // type of output
            .type(String.class).dest("type");
        parser.addArgument("out").nargs("?")
            .type(String.class).dest("out");
    }

    @Override
    public void run(Namespace details, ILogger log)
    {
        controller.export(new ExportPluginsInput(details.get("type"), details.get("out")), log);
    }
}
