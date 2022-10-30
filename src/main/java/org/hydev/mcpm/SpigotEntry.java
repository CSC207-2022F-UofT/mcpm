package org.hydev.mcpm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * Entrypoint for the Spigot plugin adapter of our program
 */
public class SpigotEntry extends JavaPlugin implements CommandExecutor
{
    private Logger log;

    /**
     * onEnable() is called when our plugin is loaded on a Spigot server.
     */
    @Override
    public void onEnable()
    {
        // Initialize logger
        log = getLogger();
        log.info("Enabled!");

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
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        sender.sendMessage("This command is not yet implemented, meow~");
        return false;
    }
}
