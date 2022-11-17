package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.CommandEntry;
import org.hydev.mcpm.client.commands.entries.EchoEntry;
import org.jetbrains.annotations.Nullable;

/**
 * Demo parser object. EchoParser has one argument "text."
 * When the user runs the echo command, the "text" argument that the user entered is passed through
 * to the build method, which returns an EchoEntry object!
 * <p>
 * The EchoEntry object is then passed to Controller, which will pass it to EchoCommand
 * (since EchoCommand extends Command&lt;EchoEntry&gt;).
 */
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
