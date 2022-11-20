package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for UnitConverter
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-19
 */
class UnitConverterTest
{
    @Test
    void autoBinarySize()
    {
        assert UnitConverter.autoBinarySize(10).unit().equals("B");
        assert UnitConverter.autoBinarySize(1000_0).unit().equals("KiB");
        assert UnitConverter.autoBinarySize(1000_000_00d).unit().equals("MiB");
        assert UnitConverter.autoBinarySize(1000_000_000_0d).unit().equals("GiB");
        assert UnitConverter.autoBinarySize(1000_000_000_000_00d).unit().equals("TiB");
    }

    @Test
    void binarySpeedFormatter()
    {
        var fmt = UnitConverter.binarySpeedFormatter();
        assert fmt.apply(10d)
            .equals(" 10.0   B/s");
        assert fmt.apply(1000_0d)
            .equals("  9.8 KiB/s");
        assert fmt.apply(1000_000_00d)
            .equals(" 95.4 MiB/s");
        assert fmt.apply(1000_000_000d)
            .equals("953.7 MiB/s");
        assert fmt.apply(1000_000_000_0d)
            .equals("  9.3 GiB/s");
        assert fmt.apply(1000_000_000_000_00d)
            .equals(" 90.9 TiB/s");
    }
}
