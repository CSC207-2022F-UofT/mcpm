package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.ReloadController;
import org.hydev.mcpm.client.interaction.ILogger;

/**
 * Argument parser for ReloadCommand. See ReloadEntry.
 */
public record ReloadParser(ReloadController controller) implements CommandParser
{
    @Override
    public String description()
    {
        return "Reload a currently loaded plugin";
    }

    @Override
    public String name()
    {
        return "reload";
    }

    @Override
    public void configure(Subparser parser)
    {
        parser.addArgument("plugins").dest("plugins").nargs("+")
            .help("Name of the plugins to reload");
    }

    @Override
    public void run(Namespace details, ILogger log)
    {
        controller.reload(details.get("plugins"), log);
    }
}
