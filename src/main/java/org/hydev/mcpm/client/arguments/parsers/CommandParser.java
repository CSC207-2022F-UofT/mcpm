package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.CommandEntry;
import org.jetbrains.annotations.Nullable;

public interface CommandParser {
    @Nullable
    Subparser configure(Subparsers parsers);

    CommandEntry build(Namespace details);
}
