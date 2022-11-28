package org.hydev.mcpm.utils;

import org.bukkit.command.CommandSender;

import java.io.PrintStream;
import java.util.Map;
import java.util.function.Consumer;

import static org.hydev.mcpm.utils.Sugar.uncheckedMapOf;

/**
 * Logging with minecraft color codes
 * (see <a href="https://minecraft.fandom.com/wiki/Formatting_codes">formatting codes</a>)
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
     * Println to stdout with colors
     *
     * @param text Text with color codes
     */
    public static void printc(String text)
    {
        System.out.println(encodeAnsiString(text));
    }

    /**
     * Println to stdout with colors and formatting
     *
     * @param format Format string
     * @param args Format arguments
     */
    public static void printfc(String format, Object... args)
    {
        printc(String.format(format, args));
    }

    /**
     * Create a logger that sends colored messages to a minecraft player
     *
     * @param sender Minecraft CommandSender
     * @return Logger function
     */
    public static Consumer<String> toMinecraft(CommandSender sender)
    {
        return s -> sender.sendMessage(s == null ? "null" : s.replace("&", "§"));
    }

    /**
     * Colorize string with ANSI escape sequences for color output to stdout
     *
     * @param in Input string
     * @return Colored string
     */
    private static String encodeAnsiString(String in)
    {
        if (in == null) return "null";

        // Add &r to the end
        in += "&r";

        // Escape ampersand
        var ampEscape = "[gFk38N EsCaPeD AmPeRsAnD f47Svw]";
        in = in.replace("&&", ampEscape);

        // Replace colors
        for (var entry : ansiReplacements.entrySet())
        {
            in = in.replace(entry.getKey(), entry.getValue());
        }

        return in.replace(ampEscape, "&");
    }

    /**
     * Create a logger that prints colored messages to a standard print stream
     *
     * @param stream Print stream (can be stdout or stderr)
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
        if (in == null) return 0;
        for (var key : ansiReplacements.keySet()) {
            in = in.replace(key, "");
        }
        return in.length();
    }
}
