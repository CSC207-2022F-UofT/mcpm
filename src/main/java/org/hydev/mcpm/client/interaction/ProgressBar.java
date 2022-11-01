package org.hydev.mcpm.client.interaction;

import org.fusesource.jansi.AnsiConsole;
import org.hydev.mcpm.utils.ConsoleUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.fusesource.jansi.internal.CLibrary.STDOUT_FILENO;
import static org.fusesource.jansi.internal.CLibrary.isatty;
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
    private int cols;

    private final List<ProgressRow> activeBars;

    private long lastUpdate;

    private double frameDelay;

    private final boolean istty;

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

        // Default to 70-char width if the width can't be detected (like in a non-tty output)
        if (this.cols == 0) this.cols = 70;

        // Last update time
        this.lastUpdate = System.nanoTime();

        // Default frame delay is 0.01666 (60 fps)
        this.frameDelay = 1 / 60d;

        // Check if output is a TTY. If not, change frame rate to 0.5 fps to avoid spamming a log.
        this.istty = isatty(STDOUT_FILENO) == 0;
        if (istty) this.frameDelay = 1 / 0.5;
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
        // Check time to limit for framerate (default 60fps)
        // Performance of the update heavily depends on the terminal's escape code handling
        // implementation, so frequent updates will degrade performance on a bad terminal
        var curTime = System.nanoTime();
        if ((curTime - lastUpdate) / 1e9d < frameDelay) return;
        lastUpdate = curTime;

        forceUpdate();
    }

    private void forceUpdate()
    {
        // Roll back to the first line
        if (istty) cu.curUp(activeBars.size());
        activeBars.forEach(bar -> out.println(bar.toString(theme, cols)));
    }

    /**
     * Finish a progress bar
     *
     * @param bar Progress bar
     */
    public void finishBar(ProgressRow bar)
    {
        if (!activeBars.contains(bar)) return;

        forceUpdate();
        this.activeBars.remove(bar);
    }

    /**
     * Finalize and close the progress bar (print the final line)
     */
    @Override
    public void close()
    {
    }

    public ProgressBar setFrameDelay(double frameDelay)
    {
        this.frameDelay = frameDelay;
        return this;
    }

    /**
     * Set frame rate in the unit of frames per second
     *
     * @param fps FPS
     * @return Self for fluent access
     */
    public ProgressBar setFps(int fps)
    {
        this.frameDelay = 1d / fps;
        return this;
    }

    public List<ProgressRow> getActiveBars()
    {
        return activeBars;
    }

    /**
     * Displays a demo progress bar.
     *
     * @param args Arguments are ignored.
     */
    public static void main(String[] args)
    {
        try (var b = new ProgressBar(ProgressBarTheme.ASCII_THEME))
        {
            var all = new ArrayList<ProgressRow>();
            for (int i = 0; i < 1300; i++)
            {
                if (i < 1000 && i % 100 == 0) {
                    var row = new ProgressRow(300)
                        .unit("MB")
                        .desc(format("File %s.tar.gz", all.size()))
                        .descLen(30);

                    all.add(b.appendBar(row));
                }

                all.forEach(a -> a.increase(1));
                safeSleep(3);
            }

            System.out.println("Done");
        }
    }
}
