package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hydev.mcpm.utils.ColorLogger.printc;
import static org.hydev.mcpm.utils.ColorLogger.printfc;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the ColorLogger class.
 */
class ColorLoggerTest
{
    @Test
    @Tag("IntegrationTest")
    void toStdOut()
    {
        var log = ColorLogger.toStdOut();
        log.accept("&aGreen! &cRed! &bBlue! &rDefault!");
        printc("&aPrintc!");
        printfc("&aPrintfc! %.2f", 0.031f);
    }

    @Test
    @Tag("IntegrationTest")
    void lengthNoColor()
    {
        assertEquals(ColorLogger.lengthNoColor("&bBlue!"), "Blue!".length());
    }
}
