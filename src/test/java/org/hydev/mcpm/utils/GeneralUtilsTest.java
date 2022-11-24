package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assert (40 <= elapsed) && (elapsed <= 60);
    }

    @Test
    void getResourceFile()
    {
        var absPath = GeneralUtils.getResourceFile("test-plugin-activelist.jar");
        assertNotNull(absPath);
        assert absPath.isFile();
        assert absPath.toString().endsWith("test-plugin-activelist.jar");
    }
}
