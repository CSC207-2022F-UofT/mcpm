package org.hydev.mcpm.utils.arrays;

import org.hydev.mcpm.utils.Pair;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * An implementation of FixedWindowSum using a queue.
 *
 * @author Peter (https://github.com/MstrPikachu)
 * @since 2022-11-21
 */
public class SlidingWindow implements FixedWindowSum {
    private long sum;
    private long window;
    private final Queue<Pair<Long, Long>> queue = new ArrayDeque<>();

    @Override
    public FixedWindowSum setWindowSize(long window) {
        this.window = window;
        return this;
    }

    @Override
    public void add(long index, long inc) {
        queue.add(new Pair<>(index, inc));
        sum += inc; // increase sum after adding a new value
        restore(index);
    }

    @Override
    public long sum() {
        return sum;
    }

    @Override
    public long sum(long index) {
        restore(index);
        return sum;
    }

    /**
     * Restore the invariant that all values in the queue are within the window
     *
     * @param index index of the newest query/insert
     */
    private void restore(long index) {
        // remove values outside the window, O(1) amortized
        while (!queue.isEmpty() && queue.peek().getKey() < index - window) {
            var element = queue.poll(); // remove the oldest value if it is outside the window
            sum -= element.getValue(); // decrement sum after removing values
        }
    }
}
