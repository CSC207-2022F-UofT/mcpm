package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.UnloadController;
import org.hydev.mcpm.client.interaction.ILogger;

/**
 * Argument parser for UnloadCommand. See UnloadEntry.
 */
public record UnloadParser(UnloadController controller) implements CommandParser
{
    @Override
    public String name()
    {
        return "unload";
    }

    @Override
    public String description()
    {
        return "Unload a currently loaded plugin";
    }

    @Override
    public void configure(Subparser parser)
    {
        parser.addArgument("plugins").dest("plugins").nargs("+")
            .help("Name of the plugins to unload");
    }

    @Override
    public void run(Namespace details, ILogger log)
    {
        controller.unload(details.getList("plugins"), log);
    }
}
