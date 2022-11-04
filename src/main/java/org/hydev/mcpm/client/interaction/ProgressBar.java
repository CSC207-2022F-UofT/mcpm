package org.hydev.mcpm.client.interaction;

import org.fusesource.jansi.AnsiConsole;
import org.hydev.mcpm.utils.ConsoleUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
public class ProgressBar implements ProgressBarBoundary {
    private final ConsoleUtils cu;
    private final ProgressBarTheme theme;
    private final PrintStream out;
    private int cols;

    private final List<ProgressRowBoundary> activeBars;
    private final Set<Integer> activeIds;

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
    public ProgressRowBoundary appendBar(long total)
    {
        int id = this.activeBars.size();
        this.activeIds.add(id);

        ProgressRowBoundary bar = new ProgressRow(total, id);
        this.activeBars.add(bar);
        bar.setPb(this);

        out.println();
        update();
        return bar;
    }

    @Override
    public void incrementBarProgress(int id, long inc) {
        this.activeBars.get(id).increase(inc);
    }

    @Override
    public void setBarProgress(int id, long progress) {
        this.activeBars.get(id).set(progress);
    }

    @Override
    public void update()
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
        int prev = -1;
        for (int i = 0; i < activeBars.size(); i ++){
            int curdown = i - prev - 1;
            prev = i;
            cu.curUp(-curdown);
            cu.eraseLine();
            out.println(activeBars.get(i).toString(theme, cols));
        }
    }

    @Override
    public void finishBar(int id)
    {
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

    @Override
    public ProgressBarBoundary setFrameDelay(double frameDelay)
    {
        this.frameDelay = frameDelay;
        return this;
    }

    @Override
    public ProgressBarBoundary setFps(int fps)
    {
        this.frameDelay = 1d / fps;
        return this;
    }

    public List<ProgressRowBoundary> getActiveBars()
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
                    var row = b.appendBar(300)
                        .unit("MB")
                        .desc(format("File %s.tar.gz", all.size()))
                        .descLen(30);

                    all.add(row.getId());
                }

                for (int j = 0; j < all.size(); j ++)
                    b.incrementBarProgress(j, 4 * j % 11 + 1);
                safeSleep(3);
            }

            System.out.println("Done");
        }
    }
}
