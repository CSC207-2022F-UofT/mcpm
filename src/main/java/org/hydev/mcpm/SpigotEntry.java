package org.hydev.mcpm;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Entrypoint for the Spigot plugin adapter of our program
 */
public class SpigotEntry extends JavaPlugin
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
        log.info("[MCPM] Enabled!");
    }

    /**
     * onDisable() is called when our plugin is unloaded on a Spigot server.
     */
    @Override
    public void onDisable()
    {
        log.info("[MCPM] Disabled!");
    }
}
