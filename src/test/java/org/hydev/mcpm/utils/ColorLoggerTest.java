package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        log.accept("&&a escaped! &&&a not escaped!");
    }
}
