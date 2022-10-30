package org.hydev.mcpm.client.interaction;

import org.fusesource.jansi.AnsiConsole;
import org.hydev.mcpm.utils.ConsoleUtils;

import java.io.PrintStream;
import java.util.*;

import static java.lang.String.format;
import static org.hydev.mcpm.utils.GeneralUtils.safeSleep;

/**
 * Terminal progress bar based on Xterm escape codes
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class ProgressBar implements AutoCloseable
{
    private final ConsoleUtils cu;
    private final ProgressBarTheme theme;
    private final PrintStream out;
    private final int cols;

    private final List<ProgressRow> activeBars;

    /**
     * Create and initialize a progress bar
     *
     * @param theme Selected theme
     */
    public ProgressBar(ProgressBarTheme theme)
    {
        this.theme = theme;
        this.out = System.out;
        this.cu = new ConsoleUtils(this.out);
        this.activeBars = new ArrayList<>();
        this.cols = AnsiConsole.getTerminalWidth();
    }

    /**
     * Append a progress bar at the end
     *
     * @param bar Row of the progress bar
     * @return bar for fluent access
     */
    public ProgressRow appendBar(ProgressRow bar)
    {
        this.activeBars.add(bar);
        bar.setPb(this);

        out.println();
        update();
        return bar;
    }

    protected void update()
    {
        // Roll back to the first line
        cu.curUp(activeBars.size());
        activeBars.forEach(bar -> out.println(bar.fmt(theme, cols)));
    }

    /**
     * Finish a progress bar
     *
     * @param bar Progress bar
     */
    public void finishBar(ProgressRow bar)
    {
        this.activeBars.remove(bar);
    }

    /**
     * Finalize and close the progress bar (print the final line)
     *
     * @throws Exception e
     */
    @Override
    public void close()
    {
    }

    public List<ProgressRow> getActiveBars()
    {
        return activeBars;
    }

    public static void main(String[] args)
    {
        try (var b = new ProgressBar(ProgressBarTheme.ASCII_THEME))
        {
            //var r = b.appendBar(new ProgressRow(1000, "it"));
            //for (int i = 0; i < 1000; i++)
            //{
            //    r.increase(1);
            //    safeSleep(5);
            //}

            var all = new ArrayList<ProgressRow>();
            for (int i = 0; i < 1300; i++)
            {
                if (i < 1000 && i % 100 == 0) all.add(b.appendBar(new ProgressRow(300, "it")));
                all.forEach(a -> a.increase(1));
                safeSleep(3);
            }

            System.out.println("Done");
        }
    }
}
