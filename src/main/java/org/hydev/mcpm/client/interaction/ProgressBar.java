package org.hydev.mcpm.client.interaction;

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

    private final List<ProgressRowBoundary> bars;
    private final ArrayList<ProgressRowBoundary> activeBars;
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
        this.activeBars = new ArrayList<>();
        this.id = new HashMap<>();
        this.cols = AnsiConsole.getTerminalWidth();


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
        this.activeBars.add(bar);

        this.id.put(bar, id);
        this.bars.add(bar);
        bar.setPb(this);

        out.println();
        update();
        return bar;
    }


    /*
     * Reprint all the ProgresRows
     * If printing would overcap the framerate, then skip this update
     */
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

    /*
    This is where all the printing happens
     */
    private void forceUpdate()
    {
        // Roll back to the first line
        Collections.sort(activeBars, (a, b) -> Double.compare(b.getCompletion(), a.getCompletion()));
        if (istty) cu.curUp(activeBars.size());
        activeBars.forEach(bar -> {
            cu.eraseLine();
            out.println(bar.toString(theme, cols));
        });
    }

    @Override
    public void finishBar(ProgressRowBoundary bar)
    {
        if (!activeBars.contains(bar)) return;

        forceUpdate();
        this.activeBars.remove(bar);
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
                    var row = new ProgressRow(300 * 1_000_000)
                        .desc(format("File %s.tar.gz", all.size()))
                        .descLen(30);

                    b.appendBar(row);
                    all.add(row);
                }

                for (int j = 0; j < all.size(); j++) {
                    all.get(j).increase(1_000_000);
                }
                safeSleep(3);
            }

            System.out.println("Done 1");
        }
        try (var b = new ProgressBar(ProgressBarTheme.CLASSIC_THEME))
        {
            var all = new ArrayList<ProgressRowBoundary>();
            for (int i = 0; i < 36; i++) {
                ProgressRow bar = new ProgressRow(300 * 1_000_000).desc(String.format("File %s.tar.gz", i)).descLen(30);
                b.appendBar(bar);
                all.add(bar);
            }
            for (int t = 0; t < 300; t++) {
                for (int i = 0; i < 36; i++) {
                    double speed = Math.cos(Math.PI / 18 * i);
                    speed = speed * speed * 5 + 1;
                    all.get(i).increase((long) Math.ceil(speed) * 1_000_000);
                }
                safeSleep(15);
            }
        }

        try (var b = new ProgressBar(ProgressBarTheme.FLOWER_THEME)) {
            var all = new ArrayList<ProgressRowBoundary>();
            for (int i = 0; i < 36; i++) {
                ProgressRow bar = new ProgressRow(300 * 1_000_000).desc(String.format("File %s.tar.gz", i)).descLen(30);
                b.appendBar(bar);
                all.add(bar);
            }

            for (int t = 0; t < 400; t++) {
                for (int i = 0; i < 36; i++) {
                    double speed = Math.cos(Math.PI / 18 * (i + t * 36 / 150));
                    speed = Math.abs(speed) * 5;
                    all.get(i).increase((long) Math.ceil(speed * 1_000_000));
                }
                safeSleep(30);
            }
        }
    }
}
