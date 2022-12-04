package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Pair class
 */
class PairTest
{
    @Test
    void getters()
    {
        var p = Pair.of("a", 1);

        assertEquals(p.k(), p.getKey());
        assertEquals(p.k(), "a");
        assertEquals(p.v(), p.getValue());
        assertEquals(p.v(), 1);
    }

    @Test
    void setterFail()
    {
        var p = Pair.of("a", 1);
        assertThrows(Pair.UnmodifiableException.class, () -> p.setValue(1));
    }

    @Test
    void toMap()
    {
        var ps = List.of(Pair.of("a", 1), Pair.of("b", 2));
        var map = ps.stream().collect(Pair.toMap());

        assertTrue(map.containsKey("a") && map.containsKey("b"));
        assertEquals(map.get("a"), 1);
        assertEquals(map.get("b"), 2);
    }
}
