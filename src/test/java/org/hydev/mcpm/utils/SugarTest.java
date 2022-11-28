package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the various methods of the Sugar class.
 */
class SugarTest
{
    @Test
    void sub()
    {
        var a = new Integer[] {1, 2, 3, 4, 5};
        assertArrayEquals(Sugar.sub(a, 0, 5), a);
        assertArrayEquals(Sugar.sub(a, 0, 4), new Integer[] {1, 2, 3, 4});
        assertArrayEquals(Sugar.sub(a, 1, 5), new Integer[] {2, 3, 4, 5});
        assertArrayEquals(Sugar.sub(a, 1, 4), new Integer[] {2, 3, 4});
        assertEquals(Sugar.sub(a, 5, 0).length, 0);

        // Negatives
        assertArrayEquals(Sugar.sub(a, -2, 5), new Integer[] {4, 5});
        assertArrayEquals(Sugar.sub(a, -2, -1), new Integer[] {4});
        assertArrayEquals(Sugar.sub(a, 0, -1), new Integer[] {1, 2, 3, 4});
    }

    @Test
    void subFrom()
    {
        var a = new Integer[] {1, 2, 3, 4, 5};
        assertArrayEquals(Sugar.subFrom(a, 0), a);
        assertArrayEquals(Sugar.subFrom(a, 1), new Integer[] {2, 3, 4, 5});
        assertArrayEquals(Sugar.subFrom(a, -2), new Integer[] {4, 5});
        assertEquals(Sugar.subFrom(a, 5).length, 0);
        assertEquals(Sugar.subFrom(a, 999).length, 0);
    }

    @Test
    void subTo()
    {
        var a = new Integer[] {1, 2, 3, 4, 5};
        assertArrayEquals(Sugar.subTo(a, 5), a);
        assertArrayEquals(Sugar.subTo(a, 4), new Integer[] {1, 2, 3, 4});
        assertArrayEquals(Sugar.subTo(a, -2), new Integer[] {1, 2, 3});
        assertEquals(Sugar.subTo(a, -99).length, 0);
    }

    @Test
    void uncheckedMapOf()
    {
        Map<String, Integer> map = Sugar.uncheckedMapOf("a", 1, "b", 2);
        assertTrue(map.containsKey("a") && map.containsKey("b"));
        assertEquals(map.get("a"), 1);
        assertEquals(map.get("b"), 2);
    }
}
