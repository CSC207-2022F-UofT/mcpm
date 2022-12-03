package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.local.MirrorSelector;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hydev.mcpm.utils.NetworkUtils.ping;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * This class contains tests for the MirrorSelector class.
 * This class also makes networking requests.
 */
class MirrorSelectorTest
{
    private static final MirrorSelector mi = new MirrorSelector();
    private static final boolean hasInternet = ping(mi.mirrorListUrl()) != -1;

    @Test
    @Tag("IntegrationTest")
    void listAvailableMirrors() throws IOException
    {
        assumeTrue(hasInternet);
        System.out.println(mi.listAvailableMirrors());
        mi.listAvailableMirrors().forEach(it -> System.out.println(it.url()));
    }

    @Test
    @Tag("IntegrationTest")
    void updateMirrors() throws IOException
    {
        assumeTrue(hasInternet);
        mi.updateMirrors();
    }

    @Test
    @Tag("IntegrationTest")
    void pingMirrors() throws IOException
    {
        assumeTrue(hasInternet);
        var pings = mi.pingMirrors();
        System.out.println(pings);
        for (int i = 0; i < pings.size() - 1; i++)
        {
            assertTrue(pings.get(i).v() <= pings.get(i + 1).v());
        }
    }
}
