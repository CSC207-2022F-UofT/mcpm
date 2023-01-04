package org.hydev.mcpm.utils

import java.util.function.Function
import kotlin.math.abs

/**
 * Unit converter utility class
 */
object UnitConverter
{
    /**
     * Automatically selects the best unit to represent binary size.
     *
     * @param size Original size in bytes
     * @return Pair[Unit name, Converted value]
     */
    @JvmStatic
    fun autoBinarySize(size: Double): UnitSize
    {
        var s = size
        for (unit in listOf("B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB"))
        {
            if (abs(s) < 1024) return UnitSize(unit, s)
            s /= 1024.0
        }
        throw AssertionError("Size $s too large for formatting")
    }

    fun Number.sizeFmt() = autoBinarySize(this.toDouble())

    /**
     * Generate a speed formatter for unit conversion
     *
     * @return Speed formatter function
     */
    @JvmStatic
    fun binarySpeedFormatter(): Function<Double, String>
    {
        return Function { autoBinarySize(it).toString() }
    }

    /**
     * Pair of unit and size
     *
     * @param unit Unit (string)
     * @param size Size (decimal number) in that unit
     */
    data class UnitSize(@JvmField val unit: String, val size: Double)
    {
        override fun toString() = String.format("%5.1f %2$3s/s", size, unit)
    }
}
