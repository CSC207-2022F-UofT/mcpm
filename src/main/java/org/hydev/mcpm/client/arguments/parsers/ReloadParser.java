package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.entries.ReloadController;

import java.util.function.Consumer;

/**
 * Argument parser for ReloadCommand. See ReloadEntry.
 */
public class ReloadParser implements CommandParser
{
    private final ReloadController controller;

    public ReloadParser(ReloadController controller)
    {
        this.controller = controller;
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
    public void run(Namespace details, Consumer<String> log)
    {
        controller.reload(details.get("plugins"), log);
    }
}
