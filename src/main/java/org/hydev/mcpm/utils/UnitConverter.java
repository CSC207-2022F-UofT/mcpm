package org.hydev.mcpm.utils;

import java.util.List;
import java.util.function.Function;

/**
 * Unit converter utility class
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-19
 */
public class UnitConverter
{
    /**
     * Pair of unit and size
     *
     * @param unit Unit (string)
     * @param size Size (decimal number) in that unit
     */
    public record UnitSize(String unit, Double size) {}

    /**
     * Automatically selects the best unit to represent binary size.
     *
     * @param size Original size in bytes
     * @return Pair[Unit name, Converted value]
     */
    public static UnitSize autoBinarySize(double size)
    {
        for (var unit : List.of("B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB"))
        {
            if (Math.abs(size) < 1024) return new UnitSize(unit, size);
            size /= 1024d;
        }

        throw new AssertionError(String.format("Size %s too large for formatting", size));
    }

    /**
     * Generate a speed formatter for unit conversion
     *
     * @return Speed formatter function
     */
    public static Function<Double, String> binarySpeedFormatter()
    {
        return (d) -> {
            var size = autoBinarySize(d);
            return String.format("%5.1f %2$3s/s", size.size(), size.unit());
        };
    }
}
