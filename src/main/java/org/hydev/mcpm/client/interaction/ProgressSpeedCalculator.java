package org.hydev.mcpm.client.interaction;

import org.hydev.mcpm.utils.arrays.FixedWindowSum;
import org.hydev.mcpm.utils.arrays.SlidingWindow;

/**
 * An implementation of ProgressSpeedCalculator using a queue and sliding window method.
 * The sliding window method maintains a queue of all updates within the window.
 * All updates outside the window (in other words, too old) will be removed from the queue.
 * The sum of the queue is also maintained to avoid a loop to calculate it.
 */
public class ProgressSpeedCalculator implements ProgressSpeedBoundary {
    private final FixedWindowSum windowSum;
    private final long window;
    private final long start;

    private long total = 0; // total to calculate inc in setProgress

    /**
     * Construct a ProgressSpeedCalculator with a window size.
     *
     * @param window Time length of the window to calculate speed over, in nanoseconds
     */
    public ProgressSpeedCalculator(long window) {
        this(window, new SlidingWindow().setWindowSize(window));
    }

    /**
     * Construct a ProgressSpeedCalculator with the specified FixedWindowSum to calculate speed.
     * Window speed should be in nanoseconds.
     *
     * @param window window size of the given windowSum
     * @param windowSum the FixedWindowSum used to calculate speed
     */
    public ProgressSpeedCalculator(long window, FixedWindowSum windowSum) {
        this.window = window;
        start = System.nanoTime();
        this.windowSum = windowSum;
    }


    @Override
    public double getSpeed() {
        long time = System.nanoTime();
        long sum = windowSum.sum(time);
        long dt = Math.min(window, time - start);
        return sum / (dt / 1e9);
    }

    @Override
    public void setProgress(long progress) {
        long inc = progress - total; // inc = new total - old total
        this.total = progress; // set new total

        // Sliding window is increment based, so we will call incProgress instead
        incProgress(inc);
    }

    @Override
    public void incProgress(long inc) {
        windowSum.add(System.nanoTime(), inc);
    }
}
