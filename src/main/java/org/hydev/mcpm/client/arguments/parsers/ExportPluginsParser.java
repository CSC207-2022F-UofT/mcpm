package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.ExportPluginsController;
import org.hydev.mcpm.client.export.ExportPluginsInput;

import java.util.function.Consumer;

/**
 * Parser for the ExportPluginsBoundary interface.
 */
public class ExportPluginsParser implements CommandParser
{
    private final ExportPluginsController controller;

    public ExportPluginsParser(ExportPluginsController controller)
    {
        this.controller = controller;
    }

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
        parser.addArgument("type").nargs("?").setDefault("pastebin") // type of output
            .type(String.class).dest("type"); // of type OutputStream
        parser.addArgument("out").nargs("?")
            .type(String.class).dest("out");
    }

    @Override
    public void run(Namespace details, Consumer<String> log)
    {
        controller.export(new ExportPluginsInput(details.get("type"), details.get("out")), log);
    }
}
