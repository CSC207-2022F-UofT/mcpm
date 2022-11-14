package org.hydev.mcpm.client.interaction;

import jline.TerminalFactory;
import org.fusesource.jansi.AnsiConsole;
import org.hydev.mcpm.utils.ConsoleUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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
    private final int rows;

    private final List<ProgressRow> activeBars;
    private final SortedSet<Integer> activeIds;

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
        this.activeIds = new TreeSet<>();
        this.cols = AnsiConsole.getTerminalWidth();
        this.rows = TerminalFactory.get().getHeight();


        // Default to 70-char width if the width can't be detected (like in a non-tty output)
        if (this.cols == 0) this.cols = 70;

        // Last update time
        this.lastUpdate = System.nanoTime();

        // Default frame delay is 0.01666 (60 fps)
        this.frameDelay = 1 / 60d;

        // Check if output is a TTY. If not, change frame rate to 0.5 fps to avoid spamming a log.
        this.istty = isatty(STDOUT_FILENO) == 1;
        if (!istty) this.frameDelay = 1 / 0.5;
    }

    /**
     * Append a progress bar at the end
     *
     * @param bar Row of the progress bar
     * @return bar for fluent access
     */
    public ProgressRow appendBar(ProgressRow bar)
    {
        int id = this.activeBars.size();
        this.activeIds.add(id);

        bar.setId(id);
        this.activeBars.add(bar);
        bar.setPb(this);

        out.println();
        update();
        return bar;
    }


    public void incrementBarProgress(int id, long inc) {
        this.activeBars.get(id).increase(inc);
    }


    public void setBarProgress(int id, long progress) {
        this.activeBars.get(id).set(progress);
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
        int height = Math.min(activeBars.size(), rows);
        if (istty) cu.curUp(height);
        int prev = -1;
        for (int i : activeIds) {
            if (i > height)
                break;
            int curDown = i - prev - 1;
            prev = i;
            cu.curUp(-curDown); // move cursor down to next active bar
            cu.eraseLine();
            out.println(activeBars.get(i).toString(theme, cols)); // println adds a newline which is why we -1 above
        }
        cu.curUp(-(activeBars.size() - prev - 1)); // move cursor down to the bottom
    }

    /**
     * Finish a progress bar
     *
     * @param bar Progress bar
     */
    public void finishBar(ProgressRow bar)
    {
        finishBar(bar.getId());
    }

    /**
     * Finish a progress bar
     *
     * @param id Progress bar id
     */
    public void finishBar(int id) {
        if (!activeIds.contains(id)) return;

        forceUpdate();
        this.activeIds.remove(id);
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
            var all = new ArrayList<Integer>();
            for (int i = 0; i < 1300; i++)
            {
                if (i < 1000 && i % 100 == 0) {
                    var row = new ProgressRow(300)
                        .unit("MB")
                        .desc(format("File %s.tar.gz", all.size()))
                        .descLen(30);

                    b.appendBar(row);
                    all.add(row.getId());
                }

                for (int j = 0; j < all.size(); j++) {
                    b.incrementBarProgress(j, 1);
                }
                safeSleep(3);
            }

            System.out.println("Done 1");
        }
        try (var b = new ProgressBar(ProgressBarTheme.CLASSIC_THEME))
        {
            for (int i = 0; i < 36; i++) {
                ProgressRow bar = new ProgressRow(300).unit("MB").desc(String.format("File %s.tar.gz", i)).descLen(30);
                b.appendBar(bar);
            }
            for (int t = 0; t < 1000; t++) {
                for (int i = 0; i < 36; i++) {
                    double speed = Math.cos(Math.PI / 18 * i);
                    speed = speed * speed * 5 + 1;
                    b.incrementBarProgress(i, (long) Math.ceil(speed));
                }
                safeSleep(3);
            }
        }
    }
}
