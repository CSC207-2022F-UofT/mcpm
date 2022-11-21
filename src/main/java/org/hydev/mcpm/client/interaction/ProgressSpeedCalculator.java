package org.hydev.mcpm.client.interaction;

import org.hydev.mcpm.utils.Pair;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * An implementation of ProgressSpeedCalculator using a queue and sliding window method.
 *
 * The sliding window method maintains a queue of all updates within the window.
 * All updates outside the window (in other words, too old) will be removed from the queue.
 * The sum of the queue is also maintained to avoid a loop to calculate it.
 *
 * @author Peter (https://github.com/MstrPikachu)
 * @since 2022-11-19
 */
public class ProgressSpeedCalculator implements ProgressSpeedBoundary {
    private final Queue<Pair<Long, Long>> queue = new ArrayDeque<>();
    private final long window;
    private final long start;

    private long total = 0; // total to calculate inc in setProgress
    private long sum = 0; // sliding window sum

    /**
     *
     * @param window Time length of the window to calculate speed over, in nanoseconds
     */
    public ProgressSpeedCalculator(long window) {
        start = System.nanoTime();
        this.window = window;
    }


    @Override
    public double getSpeed() {
        long time = System.nanoTime();
        while (!queue.isEmpty() && queue.peek().k() < time - window) { // remove all elements that are too old
            var e = queue.remove();
            sum -= e.v(); // update sliding window sum
        }
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
        queue.add(new Pair<>(System.nanoTime(), inc));
        sum += inc;
    }
}
