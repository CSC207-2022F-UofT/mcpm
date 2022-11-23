package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.entries.UnloadController;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

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
    public void run(Namespace details, Consumer<String> log)
    {
        controller.unload(details.getList("plugins"), log);
    }
}
