package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.entries.RefreshController;

import java.util.function.Consumer;

/**
 * Parser for the refresh command
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-24
 */
public record RefreshParser(RefreshController controller) implements CommandParser
{
    @Override
    public String name()
    {
        return "refresh";
    }

    @Override
    public String description()
    {
        return "Refresh cached plugin database";
    }

    @Override
    public void configure(Subparser parser)
    {

    }

    @Override
    public void run(Namespace details, Consumer<String> log)
    {
        controller.refresh();
    }
}
