package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.entries.ExportPluginsController;
import org.hydev.mcpm.client.database.inputs.ExportPluginsInput;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * Parser for the ExportPluginsBoundary interface.
 */
public class ExportPluginsParser implements CommandParser {
    private ExportPluginsController controller;

    public ExportPluginsParser(ExportPluginsController controller) {
        this.controller = controller;
    }

    @Override
    public @Nullable Subparser configure(Subparsers parsers) {
        var parser = parsers.addParser("export"); // name of command

        parser.addArgument("outfile") // add optional output file
                .type(OutputStream.class).dest("outfile"); // of type OutputStream
        parser.addArgument("-c", "--cache")
                .type(boolean.class).dest("cache");

        return parser;
    }

    @Override
    public void run(Namespace details, Consumer<String> log) {
        controller.export(new ExportPluginsInput(details.get("cache"), details.get("outfile")), log);
    }
}