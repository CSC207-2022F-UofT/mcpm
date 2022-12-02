package org.hydev.mcpm.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Syntax sugar that java lacks
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-30
 */
public class Sugar
{
    /**
     * Subarray, behaves exactly like python's arr[start:end]
     *
     * @param <T> Type of the array
     * @param arr Array
     * @param start Start (inclusive, negative numbers means (len - n))
     * @param end End (non-inclusive, negative numbers means (len - n))
     * @return Subarray
     */
    public static <T> T[] sub(T[] arr, int start, int end)
    {
        if (start < 0) start = arr.length + start;
        if (start < 0) start = 0;
        if (end < 0) end = arr.length + end;
        if (end < 0) end = 0;
        if (start > end) start = end;
        return Arrays.copyOfRange(arr, start, end);
    }

    /**
     * Subarray from start, behaves exactly like python's arr[start:]
     *
     * @param <T> Type of the array
     * @param arr Array
     * @param start Start (inclusive, negative numbers means (len - n))
     * @return Subarray
     */
    public static <T> T[] subFrom(T[] arr, int start)
    {
        return sub(arr, start, arr.length);
    }

    /**
     * Subarray to end, behaves exactly like python's arr[:end]
     *
     * @param <T> Type of the array
     * @param arr Array
     * @param end End (non-inclusive, negative numbers means (len - n))
     * @return Subarray
     */
    public static <T> T[] subTo(T[] arr, int end)
    {
        return sub(arr, 0, end);
    }

    /**
     * Create a hashmap of values. Note: This function uses an unchecked cast, which assumes that the caller is giving
     * values of the correct type to the function.
     *
     * @param values Map content (Precondition: len(values) % 2 == 0, and each key K is followed by its value V)
     * @param <K> Type of the key
     * @param <V> Type of the value
     * @return Hashmap of values
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> uncheckedMapOf(Object... values)
    {
        var map = new HashMap<>();
        for (int i = 0; i < values.length; i += 2)
        {
            var k = values[i];
            var v = values[i + 1];

            assert k != null;
            if (v != null) map.put(k, v);
        }

        return (Map<K, V>) map;
    }
}
