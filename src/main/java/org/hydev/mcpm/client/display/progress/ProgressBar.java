package org.hydev.mcpm.client.display.progress;

import org.fusesource.jansi.AnsiConsole;
import org.hydev.mcpm.client.display.ProgressBarBoundary;
import org.hydev.mcpm.client.display.ProgressRowBoundary;
import org.hydev.mcpm.utils.ConsoleUtils;

import java.io.PrintStream;
import java.util.ArrayList;

import static org.fusesource.jansi.internal.CLibrary.STDOUT_FILENO;
import static org.fusesource.jansi.internal.CLibrary.isatty;

/**
 * Terminal progress bar based on Xterm escape codes
 */
public class ProgressBar implements ProgressBarBoundary {
    private final ConsoleUtils cu;
    private final ProgressBarTheme theme;
    private final PrintStream out;
    private int cols;

    private final ArrayList<ProgressRowBoundary> activeBars;

    private long lastUpdate;
    private double frameDelay;

    private final boolean isTty;

    private boolean closed = false;

    /**
     * Create and initialize a progress bar
     *
     * @param theme Selected theme
     */
    public ProgressBar(ProgressBarTheme theme)
    {
        this.theme = theme;
        this.out = ConsoleUtils.RAW_OUT;
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
        this.isTty = isatty(STDOUT_FILENO) == 1;
        if (!isTty) this.frameDelay = 1 / 0.5;
    }

    @Override
    public ProgressRowBoundary appendBar(ProgressRowBoundary bar)
    {
        this.activeBars.add(bar);

        bar.setPb(this);

        out.println();
        update();
        return bar;
    }


    /*
     * Reprint all the ProgressRows.
     * If printing would over do the framerate, then we will skip this update.
     */
    @Override
    public void update()
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
        activeBars.sort((a, b) -> Double.compare(b.getCompletion(), a.getCompletion()));
        if (isTty) cu.curUp(activeBars.size());
        activeBars.forEach(bar -> {
            cu.eraseLine();
            out.println(bar.toString(theme, cols));
        });
    }

    // This should be used, but I don't exactly want to put the right stuff.
    @Override
    @SuppressWarnings("unused")
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

    /**
     * Set the progress bar's frame delay.
     *
     * @param frameDelay The frame delay in seconds.
     * @return The current progress bar (this for chaining).
     */
    @SuppressWarnings("unused")
    public ProgressBar setFrameDelay(double frameDelay)
    {
        this.frameDelay = frameDelay;
        return this;
    }

    /**
     * Set the progress bar's frames per second.
     *
     * @param fps The amount of frames per second the bar should send update.
     * @return The current progress bar (this for chaining).
     */
    @SuppressWarnings("unused")
    public ProgressBar setFps(int fps)
    {
        this.frameDelay = 1d / fps;
        return this;
    }
}
