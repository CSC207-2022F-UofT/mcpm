package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for UnitConverter
 */
class UnitConverterTest
{
    @Test
    void autoBinarySize()
    {
        assertEquals(UnitConverter.autoBinarySize(10).unit, "B");
        assertEquals(UnitConverter.autoBinarySize(1000_0).unit, "KiB");
        assertEquals(UnitConverter.autoBinarySize(1000_000_00d).unit, "MiB");
        assertEquals(UnitConverter.autoBinarySize(1000_000_000_0d).unit, "GiB");
        assertEquals(UnitConverter.autoBinarySize(1000_000_000_000_00d).unit, "TiB");
    }

    @Test
    void binarySpeedFormatter()
    {
        var fmt = UnitConverter.binarySpeedFormatter();
        assertEquals(fmt.apply(10d), " 10.0   B/s");
        assertEquals(fmt.apply(1000_0d), "  9.8 KiB/s");
        assertEquals(fmt.apply(1000_000_00d), " 95.4 MiB/s");
        assertEquals(fmt.apply(1000_000_000d), "953.7 MiB/s");
        assertEquals(fmt.apply(1000_000_000_0d), "  9.3 GiB/s");
        assertEquals(fmt.apply(1000_000_000_000_00d), " 90.9 TiB/s");
    }
}
