package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for org.hydev.mcpm.utils.GeneralUtils
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-02
 */
class GeneralUtilsTest
{
    @Test
    void makeUrl()
    {
        assertEquals(GeneralUtils.makeUrl("https://example.com", "cat", "meow", "azalea", "cute").toString(),
            "https://example.com?cat=meow&azalea=cute");
    }

    @Test
    void safeSleep()
    {
        long time = System.currentTimeMillis();
        GeneralUtils.safeSleep(50);
        long elapsed = System.currentTimeMillis() - time;
        assertTrue((40 <= elapsed) && (elapsed <= 60));
    }

    @Test
    void getResourceFile()
    {
        var absPath = GeneralUtils.getResourceFile("test-plugin-activelist.jar");
        assertNotNull(absPath);
        assertTrue(absPath.isFile());
        assertTrue(absPath.toString().endsWith("test-plugin-activelist.jar"));
    }
}
