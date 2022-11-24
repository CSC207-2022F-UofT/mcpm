package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.entries.MirrorController;

import java.util.function.Consumer;

/**
 * Parser for the mirror selector command
 *
 * @param controller Mirror controller
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-22
 */
public record MirrorParser(MirrorController controller) implements CommandParser
{
    @Override
    public String name()
    {
        return "mirror";
    }

    @Override
    public String description()
    {
        return "Select a source (mirror) to download plugins from";
    }

    @Override
    public void run(Namespace details, Consumer<String> log)
    {
        switch (details.getString("op"))
        {
            case "ping" -> controller.ping(details.getBoolean("refresh"), log);
            case "select" -> controller.select(details.getString("host"), log);
        }
    }

    @Override
    public void configure(Subparser parser)
    {
        var sub = parser.addSubparsers();

        var ping = sub.addParser("ping")
            .help("Ping mirrors");
        ping.addArgument("-r", "--refresh").action(Arguments.storeTrue()).dest("refresh")
            .help("Refresh the mirror list database");
        ping.setDefault("op", "ping");

        var sel = sub.addParser("select")
            .help("Select a mirror");
        sel.addArgument("host").nargs("?")
            .help("Host of the mirror");
        sel.setDefault("op", "select");
    }
}
