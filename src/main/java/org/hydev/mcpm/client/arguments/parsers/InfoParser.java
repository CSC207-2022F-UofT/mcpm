package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.InfoController;
import org.hydev.mcpm.client.commands.presenters.InfoPresenter;
import org.hydev.mcpm.client.loader.PluginNotFoundException;

import java.util.function.Consumer;

/**
 * Command parser for the info use case
 */
public record InfoParser(InfoController controller, InfoPresenter presenter) implements CommandParser
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
        var name = details.getString("name");
        try
        {
            presenter.present(controller.info(name), log);
        }
        catch (PluginNotFoundException e)
        {
            log.accept("&cPlugin not " + name + " not found.");
        }
    }

    @Override
    public void configure(Subparser parser)
    {
        parser.addArgument("name")
            .help("Name of the plugin");
    }
}
