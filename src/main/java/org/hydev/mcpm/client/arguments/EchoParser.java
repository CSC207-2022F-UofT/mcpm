package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.CommandEntry;
import org.hydev.mcpm.client.commands.entries.EchoEntry;
import org.jetbrains.annotations.Nullable;

public class EchoParser implements CommandParser {
    @Override
    public @Nullable Subparser configure(Subparsers parsers) {
        var parser = parsers.addParser("echo");

        parser
            .addArgument("text")
            .dest("text");

        return parser;
    }

    @Override
    public CommandEntry build(Namespace details) {
        return new EchoEntry(details.getString("text"));
    }
}
