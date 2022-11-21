package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.hydev.mcpm.client.commands.entries.ListController;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Command parser for List command
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-20
 */
public record ListParser(ListController controller) implements CommandParser
{
    @Override
    public String name()
    {
        return "list";
    }

    @Override
    public String description()
    {
        return "List installed plugins";
    }

    @Override
    public void configure(Subparser parser)
    {
        parser.addArgument("type").choices("all", "manual", "outdated").setDefault("all").nargs("?");
    }

    @Override
    public void run(Namespace details, Consumer<String> log)
    {
        controller.listAll(details.getString("type"), log);
    }
}
