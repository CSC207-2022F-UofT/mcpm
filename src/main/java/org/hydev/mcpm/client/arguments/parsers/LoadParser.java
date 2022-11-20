package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.entries.LoadController;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;


/**
 * Argument parser for LoadCommand. See LoadEntry.
 */
public class LoadParser implements CommandParser {
    private final LoadController controller;

    public LoadParser(LoadController controller) {
        this.controller = controller;
    }

    @Override
    public @Nullable Subparser configure(Subparsers parsers) {
        var parser = parsers.addParser("load");

        parser.addArgument("plugins").dest("plugins").nargs("+")
            .help("Name of the plugins to load");

        return parser;
    }

    @Override
    public void run(Namespace details, Consumer<String> log) {
        controller.load(details.getList("plugins"), log);
    }
}
