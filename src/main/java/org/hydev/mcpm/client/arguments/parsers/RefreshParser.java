package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.RefreshController;
import org.hydev.mcpm.client.interaction.ILogger;

import java.io.IOException;

/**
 * Refresh the database cache and mirror list
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
    public void run(Namespace details, ILogger log)
    {
        try
        {
            controller.refresh();
            log.print("&aDatabase refreshed successfully!");
        }
        catch (IOException e)
        {
            log.print("&cDatabase refresh failed: " + e.getMessage());
        }
    }
}
