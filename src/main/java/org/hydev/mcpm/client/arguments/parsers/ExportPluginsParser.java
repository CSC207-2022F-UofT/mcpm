package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.ExportPluginsController;
import org.hydev.mcpm.client.export.ExportPluginsInput;

import java.io.OutputStream;
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
        parser.addArgument("outfile") // add optional output file
            .type(OutputStream.class).dest("outfile"); // of type OutputStream
        parser.addArgument("-c", "--cache")
            .type(boolean.class).dest("cache");
    }

    @Override
    public void run(Namespace details, Consumer<String> log)
    {
        controller.export(new ExportPluginsInput(details.get("cache"), details.get("outfile")), log);
    }
}
