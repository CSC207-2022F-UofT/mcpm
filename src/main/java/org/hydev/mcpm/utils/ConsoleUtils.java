package org.hydev.mcpm.utils;

import java.io.PrintStream;

/**
 * Xterm console interaction utilities
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-30
 */
public class ConsoleUtils
{
    private final PrintStream out;

    public ConsoleUtils(PrintStream out)
    {
        this.out = out;
    }

    public ConsoleUtils()
    {
        this(System.out);
    }

    /**
     * Reset the cursor to the first column (left-most position)
     */
    public void curCol1()
    {
        out.print('\r');
        out.flush();
    }

    /**
     * Move cursor up by n lines.
     *
     * @param lines n (Negative to move down)
     */
    public void curUp(int lines)
    {
        if (lines == 0) return;

        if (lines > 0) out.printf("\033[%dA", lines);
        else out.printf("\033[%dB", -lines);
        curCol1();
    }

    /**
     * Erases line from the current cursor position till the end of the line
     */
    public void eraseLine()
    {
        curCol1();
        out.print("\033[K");
        out.flush();
    }

    /**
     * Erase screen and move to top left
     */
    void clear() {
        out.print("\033[H\033[2J");
        out.flush();
    }
}
