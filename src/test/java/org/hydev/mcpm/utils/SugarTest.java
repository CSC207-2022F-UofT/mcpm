package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-30
 */
class SugarTest
{
    @Test
    void sub()
    {
        var a = new Integer[] {1, 2, 3, 4, 5};
        assert Arrays.equals(Sugar.sub(a, 0, 5), a);
        assert Arrays.equals(Sugar.sub(a, 0, 4), new Integer[] {1, 2, 3, 4});
        assert Arrays.equals(Sugar.sub(a, 1, 5), new Integer[] {2, 3, 4, 5});
        assert Arrays.equals(Sugar.sub(a, 1, 4), new Integer[] {2, 3, 4});
        assert Sugar.sub(a, 5, 0).length == 0;

        // Negatives
        assert Arrays.equals(Sugar.sub(a, -2, 5), new Integer[] {4, 5});
        assert Arrays.equals(Sugar.sub(a, -2, -1), new Integer[] {4});
        assert Arrays.equals(Sugar.sub(a, 0, -1), new Integer[] {1, 2, 3, 4});
    }

    @Test
    void subFrom()
    {
        var a = new Integer[] {1, 2, 3, 4, 5};
        assert Arrays.equals(Sugar.subFrom(a, 0), a);
        assert Arrays.equals(Sugar.subFrom(a, 1), new Integer[] {2, 3, 4, 5});
        assert Arrays.equals(Sugar.subFrom(a, -2), new Integer[] {4, 5});
        assert Sugar.subFrom(a, 5).length == 0;
        assert Sugar.subFrom(a, 999).length == 0;
    }

    @Test
    void subTo()
    {
        var a = new Integer[] {1, 2, 3, 4, 5};
        assert Arrays.equals(Sugar.subTo(a, 5), a);
        assert Arrays.equals(Sugar.subTo(a, 4), new Integer[] {1, 2, 3, 4});
        assert Arrays.equals(Sugar.subTo(a, -2), new Integer[] {1, 2, 3});
        assert Sugar.subTo(a, -99).length == 0;
    }

    @Test
    void uncheckedMapOf()
    {
        Map<String, Integer> map = Sugar.uncheckedMapOf("a", 1, "b", 2);
        assert map.containsKey("a") && map.containsKey("b");
        assert map.get("a") == 1 && map.get("b") == 2;
    }
}
