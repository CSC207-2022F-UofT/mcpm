package org.hydev.mcpm.utils;

import java.util.Arrays;

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
     * @param arr Array
     * @param start Start (inclusive, negative numbers means (len - n))
     * @param end End (non-inclusive, negative numbers means (len - n))
     * @return Subarray
     * @param <T> Type of the array
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
     * @param arr Array
     * @param start Start (inclusive, negative numbers means (len - n))
     * @return Subarray
     * @param <T> Type of the array
     */
    public static <T> T[] subFrom(T[] arr, int start)
    {
        return sub(arr, start, arr.length);
    }

    /**
     * Subarray to end, behaves exactly like python's arr[:end]
     *
     * @param arr Array
     * @param end End (non-inclusive, negative numbers means (len - n))
     * @return Subarray
     * @param <T> Type of the array
     */
    public static <T> T[] subTo(T[] arr, int end)
    {
        return sub(arr, 0, end);
    }
}
