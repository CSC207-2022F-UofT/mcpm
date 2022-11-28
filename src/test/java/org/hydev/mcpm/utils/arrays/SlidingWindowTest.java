package org.hydev.mcpm.utils.arrays;

import org.hydev.mcpm.utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlidingWindowTest {

    ArrayList<Pair<Long, Long>> arr;
    SlidingWindow slidingWindow;
    long window;


    long naiveSum(long index) {
        long ret = 0;
        for (int i = arr.size() - 1; i >= 0; i--) {
            if (arr.get(i).getKey() < index - window)
                break;
            ret += arr.get(i).getValue();
        }
        return ret;
    }

    @Test
    void testAll() {
        Random r = new Random();
        for (int window = 1; window <= 5001; window += 5) {
            this.window = window;
            long cur = 0;
            arr = new ArrayList<>();
            slidingWindow = new SlidingWindow();
            slidingWindow.setWindowSize(window);
            for (int i = 0; i < 1000; i++) {
                long x = r.nextLong();
                int ind = Math.abs(r.nextInt());
                cur += ind;
                arr.add(new Pair<>(cur, x));
                slidingWindow.add(cur, x);
                assertEquals(slidingWindow.sum(cur), naiveSum(cur));
            }
        }
    }
}