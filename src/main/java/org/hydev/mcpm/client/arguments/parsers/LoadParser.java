package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.entries.LoadController;

import java.util.function.Consumer;


/**
 * Argument parser for LoadCommand. See LoadEntry.
 */
public class LoadParser implements CommandParser
{
    private final LoadController controller;

    public LoadParser(LoadController controller)
    {
        this.controller = controller;
    }

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
