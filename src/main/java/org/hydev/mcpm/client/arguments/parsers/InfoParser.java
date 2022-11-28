package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.entries.InfoController;

import java.util.function.Consumer;

/**
 * Command parser for the info use case
 */
public record InfoParser(InfoController controller) implements CommandParser
{
    @Override
    public String name()
    {
        return "info";
    }

    @Override
    public String description()
    {
        return "Show the info of an installed plugin";
    }

    @Override
    public void run(Namespace details, Consumer<String> log)
    {
        controller.info(details.getString("name"), log);
    }

    @Override
    public void configure(Subparser parser)
    {
        parser.addArgument("name")
            .help("Name of the plugin");
    }
}
