package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.LoadController;

import java.util.function.Consumer;


/**
 * Argument parser for LoadCommand. See LoadEntry.
 */
public record LoadParser(LoadController controller) implements CommandParser
{
    @Override
    public String name()
    {
        return "load";
    }

    @Override
    public String description()
    {
        return "Load a plugin in the plugins folder";
    }

    @Override
    public void configure(Subparser parser)
    {
        parser.addArgument("plugins").dest("plugins").nargs("+")
            .help("Name of the plugins to load");
    }

    @Override
    public void run(Namespace details, Consumer<String> log)
    {
        controller.load(details.getList("plugins"), log);
    }
}
