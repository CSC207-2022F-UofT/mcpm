package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-02
 */
class GeneralUtilsTest
{
    @Test
    void makeUrl()
    {
        assert GeneralUtils.makeUrl("https://example.com", "cat", "meow", "azalea", "cute").toString()
            .equals("https://example.com?cat=meow&azalea=cute");
    }

    @Test
    void safeSleep()
    {
        long time = System.currentTimeMillis();
        GeneralUtils.safeSleep(50);
        long elapsed = System.currentTimeMillis() - time;
        assert (45 <= elapsed) && (elapsed <= 55);
    }

    @Test
    void getResourceFile()
    {
        var absPath = GeneralUtils.getResourceFile("test-plugin-activelist.jar");
        assert absPath != null;
        assert absPath.isFile();
        assert absPath.toString().endsWith("test-plugin-activelist.jar");
    }
}
