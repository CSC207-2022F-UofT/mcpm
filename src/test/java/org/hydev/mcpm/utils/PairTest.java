package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for Pair class
 */
class PairTest
{
    @Test
    void getters()
    {
        var p = new Pair<>("a", 1);

        assertEquals(p.k(), p.getKey());
        assertEquals(p.k(), "a");
        assertEquals(p.v(), p.getValue());
        assertEquals(p.v(), 1);
    }

    @Test
    void setterFail()
    {
        var p = new Pair<>("a", 1);
        assertThrows(Pair.UnmodifiableException.class, () -> p.setValue(1));
    }

    @Test
    void toMap()
    {
        var ps = List.of(new Pair<>("a", 1), new Pair<>("b", 2));
        var map = ps.stream().collect(Pair.toMap());

        assert map.containsKey("a") && map.containsKey("b");
        assertEquals(map.get("a"), 1);
        assertEquals(map.get("b"), 2);
    }
}
