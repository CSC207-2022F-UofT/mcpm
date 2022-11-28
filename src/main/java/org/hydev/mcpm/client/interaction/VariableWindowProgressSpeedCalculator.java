package org.hydev.mcpm.client.interaction;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Implementation of VariableWindowProgressRowBoundary using a prefix sum array.
 */
public class VariableWindowProgressSpeedCalculator implements VariableWindowProgressSpeedBoundary {
    private final ArrayList<Long> psa = new ArrayList<>();
    // psa of progress, where each element is total progress within the same millisecond
    // milliseconds are not necessarily consecutive, for example, if no progress was made at t=22ms
    // then psa will not contain 0 for this instance of time
    // in other words we use coordinate compression, to reduce memory usage

    private final TreeMap<Long, Integer> map = new TreeMap<>(); // maps time in milliseconds to psa indices
    private long total = 0;

    /**
     * Constructs a VariableWindowProgressSpeedCalculator with the given default window for getSpeed in nanoseconds.
     */
    public VariableWindowProgressSpeedCalculator() {
        long start = System.currentTimeMillis();

        // initialize values to avoid ArrayIndexOutOfBoundsException in incProgress
        psa.add(0L);
        map.put(start, 0);
    }

    @Override
    public double getSpeed() {
        return getSpeed((long) 2e9);
    }

    @Override
    public double getSpeed(long window) {
        long time = System.currentTimeMillis();
        var entry = map.ceilingEntry(time - window); // recall entry.getKey() is time of beginning of window
        if (entry == null) // this would represent the empty sum
            return 0;

        long sum = psa.get(psa.size() - 1) - psa.get(entry.getValue());
        long dt = Math.max(Math.min(time - entry.getKey(), window), 1); // do not go past starting time
        // avoid divide by 0
        return 1000d * sum / dt;
    }

    @Override
    public void setProgress(long progress) {
        long inc = progress - total; // inc = new total - old total
        this.total = progress; // set new total
        incProgress(inc);
    }

    @Override
    public void incProgress(long inc) {
        long time = System.currentTimeMillis();
        if (map.containsKey(time)) {
            int i = psa.size() - 1;
            psa.set(i, psa.get(i) + inc); // psa[i] += inc
        }
        else {
            long prev = psa.get(psa.size() - 1);
            map.put(time, psa.size()); // map[t] = ++i
            psa.add(prev + inc);
        }
    }
}
