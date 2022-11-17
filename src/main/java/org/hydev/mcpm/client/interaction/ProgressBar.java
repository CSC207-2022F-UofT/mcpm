package org.hydev.mcpm.client.interaction;

import jline.TerminalFactory;
import org.fusesource.jansi.AnsiConsole;
import org.hydev.mcpm.utils.ConsoleUtils;

import java.io.PrintStream;
import java.util.*;

import static java.lang.String.format;
import static org.fusesource.jansi.internal.CLibrary.STDOUT_FILENO;
import static org.fusesource.jansi.internal.CLibrary.isatty;
import static org.hydev.mcpm.utils.GeneralUtils.safeSleep;

/**
 * Terminal progress bar based on Xterm escape codes
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @author Peter (https://github.com/MstrPikachu)
 * @since 2022-09-27
 */
public class ProgressBar implements ProgressBarBoundary {
    private final ConsoleUtils cu;
    private final ProgressBarTheme theme;
    private final PrintStream out;
    private int cols;
    private final int rows;

    private final List<ProgressRowBoundary> bars;
    private final SortedSet<Integer> activeIds;
    private final Map<ProgressRowBoundary, Integer> id;

    private long lastUpdate;

    private double frameDelay;

    private final boolean istty;

    private boolean closed = false;

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
        this.bars = new ArrayList<>();
        this.activeIds = new TreeSet<>();
        this.id = new HashMap<>();
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

    @Override
    public ProgressRowBoundary appendBar(ProgressRowBoundary bar)
    {
        int id = this.bars.size();
        this.activeIds.add(id);

        this.id.put(bar, id);
        this.bars.add(bar);
        bar.setPb(this);

        out.println();
        update();
        return bar;
    }


    public void incrementBarProgress(int id, long inc) {
        this.bars.get(id).increase(inc);
    }


    public void setBarProgress(int id, long progress) {
        this.bars.get(id).set(progress);
    }


    protected void update()
    {
        // if the progress bar is closed, don't do anything
        if (closed)
            return;
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
        if (bars.size() <= rows) { // if terminal is tall enough, we can move cursor around freely
            // Roll back to the first line
            int height = bars.size();
            if (istty) cu.curUp(height);
            int prev = -1;
            for (int i : activeIds) {
                if (i > height)
                    break;
                int curDown = i - prev - 1;
                prev = i;
                cu.curUp(-curDown); // move cursor down to next active bar
                cu.eraseLine();
                out.println(bars.get(i).toString(theme, cols)); // println adds a newline which is why we -1 above
            }
            cu.curUp(-(bars.size() - prev - 1)); // move cursor down to the bottom
        }
        else { // no terminal space, print active ones only
            int i = 0;
            for (int id : activeIds) {
                out.println(bars.get(id).toString(theme, cols));
                if (++i == rows)
                    break;
            }
        }
    }

    @Override
    public void finishBar(ProgressRowBoundary bar)
    {
        finishBar(id.get(bar));
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

    @Override
    public void close()
    {
        closed = true;
    }


    @Override
    public ProgressBar setFrameDelay(double frameDelay)
    {
        this.frameDelay = frameDelay;
        return this;
    }

    @Override
    public ProgressBar setFps(int fps)
    {
        this.frameDelay = 1d / fps;
        return this;
    }

    @Override
    public List<ProgressRowBoundary> getBars()
    {
        return bars;
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
            var all = new ArrayList<ProgressRowBoundary>();
            for (int i = 0; i < 1300; i++)
            {
                if (i < 1000 && i % 100 == 0) {
                    var row = new ProgressRow(300)
                        .unit("MB")
                        .desc(format("File %s.tar.gz", all.size()))
                        .descLen(30);

                    b.appendBar(row);
                    all.add(row);
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
