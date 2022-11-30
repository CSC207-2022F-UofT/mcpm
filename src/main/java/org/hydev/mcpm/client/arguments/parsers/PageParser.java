package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.PageController;

import java.util.function.Consumer;

/**
 * Parser for the pagination command
 */
public record PageParser(PageController controller) implements CommandParser
{
    @Override
    public String name()
    {
        return "page";
    }

    @Override
    public String description()
    {
        return "";
    }

    @Override
    public void configure(Subparser parser)
    {
        parser.addArgument("page").type(Integer.class).help("Number of the page you want to view");
    }

    @Override
    public void run(Namespace details, Consumer<String> log)
    {
        var page = controller.formatPage(details.getInt("page"));
        if (page == null)
        {
            log.accept("&cNo pages available.");
            return;
        }
        log.accept(page);
    }
}
