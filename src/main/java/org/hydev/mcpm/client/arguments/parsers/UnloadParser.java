package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.entries.UnloadController;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Argument parser for UnloadCommand. See UnloadEntry.
 */
public class UnloadParser implements CommandParser {
    private final UnloadController controller;

    public UnloadParser(UnloadController controller) {
        this.controller = controller;
    }

    @Override
    public @Nullable Subparser configure(Subparsers parsers) {
        var parser = parsers.addParser("unload");

        parser.addArgument("plugins").dest("plugins").nargs("+")
            .help("Name of the plugins to unload");

        return parser;
    }

    @Override
    public void run(Namespace details, Consumer<String> log) {
        controller.unload(details.getList("plugins"), log);
    }
}
