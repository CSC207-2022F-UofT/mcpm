package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.CommandEntry;
import org.hydev.mcpm.client.commands.entries.LoadEntry;
import org.jetbrains.annotations.Nullable;


/**
 * Argument parser for LoadCommand. See LoadEntry.
 */
public class LoadParser implements CommandParser {
    @Override
    public @Nullable Subparser configure(Subparsers parsers) {
        var parser = parsers
            .addParser("load");

        parser
            .addArgument("plugins")
            .dest("plugins")
            .nargs("+");

        return parser;
    }

    @Override
    public CommandEntry build(Namespace details) {
        return new LoadEntry(
            details.getList("plugins")
        );
    }
}
