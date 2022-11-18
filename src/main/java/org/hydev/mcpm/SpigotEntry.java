package org.hydev.mcpm;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.hydev.mcpm.client.arguments.ArgsParser;
import org.hydev.mcpm.client.arguments.CommandsFactory;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * Entrypoint for the Spigot plugin adapter of our program
 */
public class SpigotEntry extends JavaPlugin implements CommandExecutor
{
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
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args)
    {
        try {
            parser.parse(args);
        } catch (ArgumentParserException e) {
            /*
             * Ignore incorrect commands?
             * Not sure if I'm supposed to complain here.
             * Previous code seemed to silently fail I think.
             */
        }

        return true;
    }
}
