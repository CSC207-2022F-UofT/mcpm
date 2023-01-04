package org.hydev.mcpm.utils

import java.io.PrintStream

/**
 * Logging with minecraft color codes
 * (see [formatting codes](https://minecraft.fandom.com/wiki/Formatting_codes))
 */
object ColorLogger
{
    private val ansiReplacements = Sugar.uncheckedMapOf<String, String>(
        "&0", "\u001b[0;30m",
        "&1", "\u001b[0;34m",
        "&2", "\u001b[0;32m",
        "&3", "\u001b[0;36m",
        "&4", "\u001b[0;31m",
        "&5", "\u001b[0;35m",
        "&6", "\u001b[0;33m",
        "&7", "\u001b[0;37m",
        "&8", "\u001b[1;30m",
        "&9", "\u001b[1;34m",
        "&a", "\u001b[1;32m",
        "&b", "\u001b[1;36m",
        "&c", "\u001b[1;31m",
        "&d", "\u001b[1;35m",
        "&e", "\u001b[1;33m",
        "&f", "\u001b[1;37m",
        "&r", "\u001b[0m",
        "&l", "\u001b[1m",
        "&o", "\u001b[3m",
        "&n", "\u001b[4m",
        "&-", "\n"
    )

    /**
     * Println to stdout with colors
     *
     * @param text Text with color codes
     */
    @JvmStatic
    fun printc(text: String)
    {
        println(encodeAnsiString(text))
    }

    /**
     * Println to stdout with colors and formatting
     *
     * @param format Format string
     * @param args Format arguments
     */
    @JvmStatic
    fun printfc(format: String?, vararg args: Any?)
    {
        printc(String.format(format!!, *args))
    }

    /**
     * Colorize string with ANSI escape sequences for color output to stdout
     *
     * @param txt Input string
     * @return Colored string
     */
    private fun encodeAnsiString(txt: String?): String
    {
        var s: String = txt ?: return "null"

        // Add &r to the end
        s += "&r"

        // Escape ampersand
        val ampEscape = "[gFk38N EsCaPeD AmPeRsAnD f47Svw]"
        s = s.replace("&&", ampEscape)

        // Replace colors
        for ((key, value) in ansiReplacements)
        {
            s = s.replace(key, value)
        }
        return s.replace(ampEscape, "&")
    }

    /**
     * Calculate string length removing color escape codes
     *
     * @param txt Input string
     * @return True length
     */
    fun trimNoColor(txt: String?): String
    {
        var s: String = txt ?: return ""
        for (key in ansiReplacements.keys)
        {
            s = s.replace(key, "")
        }
        return s
    }

    /**
     * Calculate string length removing color escape codes
     *
     * @param txt Input string
     * @return True length
     */
    @JvmStatic
    fun lengthNoColor(txt: String?) = trimNoColor(txt).length

    fun PrintStream.printc(txt: String) = println(encodeAnsiString(txt))
}


