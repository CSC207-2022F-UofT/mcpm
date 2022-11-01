package org.hydev.mcpm;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.UnloadBoundary;
import org.hydev.mcpm.client.injector.ReloadBoundary;

import java.util.function.Consumer;

import static java.lang.String.format;
import static org.hydev.mcpm.utils.Sugar.subFrom;

/**
 * Controller of the program
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-30
 */
public class Controller
{
    private final LoadBoundary loader;
    private final UnloadBoundary unloader;
    private final ReloadBoundary reloader;

    /**
     * Creates default controller with a PluginLoader for all boundaries.
     */
    public Controller()
    {
        var pl = new PluginLoader();
        loader = pl;
        unloader = pl;
        reloader = pl;
    }

    /**
     * Run a command
     *
     * @param args Command-line arguments (not including the command name/path)
     * @param log Callback logger
     */
    public void runCommand(String[] args, Consumer<String> log)
    {
        if (args.length == 0) return;

        var cmd = args[0].toLowerCase();
        switch (cmd)
        {
            case "load", "unload", "reload" ->
            {
                try
                {
                    var a = ArgsParser.LOADER.parseArgs(subFrom(args, 1));
                    var name = a.getString("name");

                    try
                    {
                        switch (cmd)
                        {
                            case "load" -> loader.loadPlugin(name);
                            case "unload" -> unloader.unloadPlugin(name);
                            case "reload" -> reloader.reloadPlugin(name);
                            default -> { }
                        }
                        log.accept(format("Plugin %s %sed", name, cmd));
                    }
                    catch (PluginNotFoundException e)
                    {
                        log.accept(format("Plugin %s not found", name));
                    }
                }
                catch (ArgumentParserException e)
                {
                    log.accept("Error: " + e.getMessage());
                    log.accept(ArgsParser.LOADER.formatHelp());
                }
            }
            default ->
            {
                log.accept("Usage: /mcpm <install/uninstall/update/search/load/unload/reload/help>");
            }
        }
    }
}
