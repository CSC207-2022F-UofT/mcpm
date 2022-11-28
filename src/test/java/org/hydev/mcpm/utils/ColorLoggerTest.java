package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import static org.hydev.mcpm.utils.ColorLogger.printc;
import static org.hydev.mcpm.utils.ColorLogger.printfc;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for ColorLogger
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-20
 */
class ColorLoggerTest
{
    @Test
    void toStdOut()
    {
        var log = ColorLogger.toStdOut();
        log.accept("&aGreen! &cRed! &bBlue! &rDefault!");
        printc("&aPrintc!");
        printfc("&aPrintfc! %.2f", 0.031f);
    }

    @Test
    void lengthNoColor()
    {
        assertEquals(ColorLogger.lengthNoColor("&bBlue!"), "Blue!".length());
    }
}
