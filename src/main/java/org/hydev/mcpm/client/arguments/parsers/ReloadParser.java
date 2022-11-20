package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.entries.ReloadController;
import org.hydev.mcpm.client.injector.ReloadBoundary;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Argument parser for ReloadCommand. See ReloadEntry.
 */
public class ReloadParser implements CommandParser {
    private final ReloadController controller;

    public ReloadParser(ReloadController controller) {
        this.controller = controller;
    }

    @Override
    public @Nullable Subparser configure(Subparsers parsers) {
        var parser = parsers.addParser("reload");

        parser.addArgument("plugins").dest("plugins").nargs("+")
            .help("Name of the plugins to reload");

        return parser;
    }

    @Override
    public void run(Namespace details, Consumer<String> log) {
        controller.reload(details.get("plugins"), log);
    }
}
