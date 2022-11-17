package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.CommandEntry;
import org.hydev.mcpm.client.commands.entries.LoadEntry;
import org.hydev.mcpm.client.commands.entries.ReloadEntry;
import org.jetbrains.annotations.Nullable;

/**
 * Argument parser for ReloadCommand. See ReloadEntry.
 */
public class ReloadParser implements CommandParser {
    @Override
    public @Nullable Subparser configure(Subparsers parsers) {
        var parser = parsers
            .addParser("reload");

        parser
            .addArgument("plugins")
            .dest("plugins")
            .nargs("+");

        return parser;
    }

    @Override
    public CommandEntry build(Namespace details) {
        return new ReloadEntry(
            details.getList("plugins")
        );
    }
}
