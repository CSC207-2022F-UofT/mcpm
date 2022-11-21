package org.hydev.mcpm.utils;

import org.bukkit.command.CommandSender;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

import static org.hydev.mcpm.utils.Sugar.uncheckedMapOf;

/**
 * Logging with minecraft color codes (see https://minecraft.fandom.com/wiki/Formatting_codes)
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-20
 */
public class ColorLogger
{
    private static final Map<String, String> ansiReplacements = uncheckedMapOf(
        "&0", "\033[0;30m",
        "&1", "\033[0;34m",
        "&2", "\033[0;32m",
        "&3", "\033[0;36m",
        "&4", "\033[0;31m",
        "&5", "\033[0;35m",
        "&6", "\033[0;33m",
        "&7", "\033[0;37m",
        "&8", "\033[1;30m",
        "&9", "\033[1;34m",
        "&a", "\033[1;32m",
        "&b", "\033[1;36m",
        "&c", "\033[1;31m",
        "&d", "\033[1;35m",
        "&e", "\033[1;33m",
        "&f", "\033[1;37m",
        "&r", "\033[0m",
        "&l", "\033[1m",
        "&o", "\033[3m",
        "&n", "\033[4m",
        "&-", "\n"
    );

    /**
     * Create a logger that sends colored messages to a minecraft player
     *
     * @param sender Minecraft CommandSender
     * @return Logger function
     */
    public static Consumer<String> toMinecraft(CommandSender sender)
    {
        return s -> sender.sendMessage(s.replace("&", "§"));
    }

    /**
     * Colorize string with ANSI escape sequences for color output to stdout
     *
     * @param in Input string
     * @return Colored string
     */
    private static String encodeAnsiString(String in)
    {
        // Add &r (color reset) after each line
        //in = String.join("\n", Arrays.stream(in.split("\n")).map(line -> line + "&r").toList());

        // Add &r to the end
        in += "&r";

        // Replace colors
        for (var entry : ansiReplacements.entrySet())
        {
            in = in.replace(entry.getKey(), entry.getValue());
        }

        return in;
    }

    /**
     * Create a logger that prints colored messages to a standard print stream
     *
     * @param stream Print stream (can be System.out or System.err)
     * @return Logger function
     */
    public static Consumer<String> toPrint(PrintStream stream)
    {
        return s -> stream.println(encodeAnsiString(s));
    }

    /**
     * Create a logger that prints colored messages to stdout
     *
     * @return Logger function
     */
    public static Consumer<String> toStdOut()
    {
        return toPrint(System.out);
    }

    /**
     * Calculate string length removing color escape codes
     *
     * @param in Input string
     * @return True length
     */
    public static int lengthNoColor(String in)
    {
        for (var key : ansiReplacements.keySet()) in = in.replace(key, "");
        return in.length();
    }
}
