package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for NetworkUtils
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-01
 */
class NetworkUtilsTest
{
    @Test
    void ping()
    {
        // Since we might run the test in an offline environment, we shouldn't assert it has connectivity
        System.out.println("Ping to 1.1.1.1 is: " + NetworkUtils.ping("1.1.1.1") + " ms");
    }
}
