package org.hydev.mcpm

import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.hydev.mcpm.client.arguments.ArgsParser
import org.hydev.mcpm.client.arguments.ArgsParserFactory
import org.hydev.mcpm.client.arguments.parsers.CommandParser
import org.hydev.mcpm.utils.ColorLogger

/**
 * Entrypoint for the Spigot plugin adapter of our program
 */
class SpigotEntry : JavaPlugin(), CommandExecutor
{
    private lateinit var parser: ArgsParser

    companion object
    {
        private lateinit var instance: SpigotEntry

        @JvmStatic
        fun instance() = instance
    }

    /**
     * onEnable() is called when our plugin is loaded on a Spigot server.
     */
    override fun onEnable()
    {
        // Initialize instance. This is needed for the PluginLoader.
        instance = this

        // Initialize logger
        logger.info("Enabled!")

        // Initialize controller
        parser = ArgsParserFactory.serverArgsParser()

        // Register mcpm command
        getCommand("mcpm")!!.setExecutor(this)
    }

    /**
     * onDisable() is called when our plugin is unloaded on a Spigot server.
     */
    override fun onDisable()
    {
        logger.info("Disabled!")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean
    {
        val log = ColorLogger.toMinecraft(sender)
        try
        {
            parser.parse(args, log)
        }
        catch (e: ArgumentParserException)
        {
            parser.fail(e, log)
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>?
    {
        if (command.name.lowercase() != "mcpm") return null
        return if (args.size == 1) parser.rawSubparsers.map { it.name() }
        else null
    }
}
