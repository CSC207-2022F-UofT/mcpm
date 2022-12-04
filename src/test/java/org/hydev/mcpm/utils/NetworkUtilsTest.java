package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for NetworkUtils
 */
class NetworkUtilsTest
{
    @Test
    @Tag("IntegrationTest")
    void ping()
    {
        // Since we might run the test in an offline environment, we shouldn't assert it has connectivity
        var ping = NetworkUtils.ping("1.1.1.1");
        System.out.println("Ping to 1.1.1.1 is: " + ping + " ms");

        assertTrue(ping >= 0);
    }
}
