package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Tests for Pair class
 */
class PairTest
{
    @Test
    void gettersSetters()
    {
        var p = new Pair<>("a", 1);

        assert p.k().equals(p.getKey());
        assert p.k().equals("a");
        assert p.v().equals(p.getValue());
        assert p.v().equals(1);

        try
        {
            p.setValue(2);
            assert false;
        }
        catch (Pair.UnmodifiableException ignored) { }
    }

    @Test
    void toMap()
    {
        var ps = List.of(new Pair<>("a", 1), new Pair<>("b", 2));
        var map = ps.stream().collect(Pair.toMap());

        assert map.containsKey("a") && map.containsKey("b");
        assert map.get("a") == 1 && map.get("b") == 2;
    }
}
