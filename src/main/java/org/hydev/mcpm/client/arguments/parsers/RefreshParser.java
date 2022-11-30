package org.hydev.mcpm.client.arguments.parsers;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.commands.controllers.RefreshController;

import java.io.IOException;
import java.util.function.Consumer;

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
    public void run(Namespace details, Consumer<String> log)
    {
        try
        {
            controller.refresh();
            log.accept("&aDatabase refreshed successfully!");
        }
        catch (IOException e)
        {
            log.accept("&cDatabase refresh failed: " + e.getMessage());
        }
    }
}
