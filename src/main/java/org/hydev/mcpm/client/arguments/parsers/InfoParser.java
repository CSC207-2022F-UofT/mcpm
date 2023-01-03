package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.InfoController;
import org.hydev.mcpm.client.commands.presenters.InfoPresenter;
import org.hydev.mcpm.client.interaction.ILogger;
import org.hydev.mcpm.client.loader.PluginNotFoundException;

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
    public void run(Namespace details, ILogger log)
    {
        var name = details.getString("name");
        try
        {
            presenter.present(controller.info(name), log);
        }
        catch (PluginNotFoundException e)
        {
            log.print(String.format("&cCannot find plugin '%s'", name));
        }
    }

    @Override
    public void configure(Subparser parser)
    {
        parser.addArgument("name")
            .help("Name of the plugin");
    }
}
