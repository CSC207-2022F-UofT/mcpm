package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.CommandEntry;
import org.hydev.mcpm.client.commands.entries.LoadEntry;
import org.hydev.mcpm.client.commands.entries.UnloadEntry;
import org.jetbrains.annotations.Nullable;

/**
 * Argument parser for UnloadCommand. See UnloadEntry.
 */
public class UnloadParser implements CommandParser {
    @Override
    public @Nullable Subparser configure(Subparsers parsers) {
        var parser = parsers
            .addParser("unload");

        parser
            .addArgument("plugins")
            .dest("plugins")
            .nargs("+");

        return parser;
    }

    @Override
    public CommandEntry build(Namespace details) {
        return new UnloadEntry(
            details.getList("plugins")
        );
    }
}
