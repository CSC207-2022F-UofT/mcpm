package org.hydev.mcpm;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.hydev.mcpm.client.arguments.ArgsParser;
import org.hydev.mcpm.client.arguments.CommandsFactory;
import org.hydev.mcpm.client.arguments.parsers.CommandParser;
import org.hydev.mcpm.utils.ColorLogger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * Entrypoint for the Spigot plugin adapter of our program
 */
public class SpigotEntry extends JavaPlugin implements CommandExecutor
{
    public SpigotEntry()
    {
        // Let the other parts of our program know that we're in minecraft
        Constants.IS_MINECRAFT = true;
    }

    private Logger log;

    private ArgsParser parser;

    /**
     * onEnable() is called when our plugin is loaded on a Spigot server.
     */
    @Override
    public void onEnable()
    {
        // Initialize logger
        log = getLogger();
        log.info("Enabled!");

        // Initialize controller
        parser = CommandsFactory.serverArgsParser(text -> log.info(text));

        // Register mcpm command
        requireNonNull(this.getCommand("mcpm")).setExecutor(this);
    }

    /**
     * onDisable() is called when our plugin is unloaded on a Spigot server.
     */
    @Override
    public void onDisable()
    {
        log.info("Disabled!");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args)
    {
        var log = ColorLogger.toMinecraft(sender);

        try
        {
            parser.parse(args, log);
        }
        catch (ArgumentParserException e)
        {
            parser.fail(e, log);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
                                      @NotNull String[] args)
    {
        if (!command.getName().equalsIgnoreCase("mcpm")) return null;

        if (args.length == 1)
        {
            return new ArrayList<>(parser.getRawSubparsers().stream().map(CommandParser::name).toList());
        }

        return null;
    }
}
