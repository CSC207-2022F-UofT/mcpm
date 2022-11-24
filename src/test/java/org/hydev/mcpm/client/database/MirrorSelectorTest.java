package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.mirrors.MirrorSelector;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hydev.mcpm.client.database.mirrors.MirrorSelector.MIRROR_LIST_URL;
import static org.hydev.mcpm.utils.NetworkUtils.ping;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-01
 */
class MirrorSelectorTest
{
    static boolean hasInternet = ping(MIRROR_LIST_URL) != -1;
    static MirrorSelector mi = new MirrorSelector();

    @Test
    void listAvailableMirrors() throws IOException
    {
        assumeTrue(hasInternet);
        System.out.println(mi.listAvailableMirrors());
        mi.listAvailableMirrors().forEach(it -> System.out.println(it.url()));
    }

    @Test
    void updateMirrors() throws IOException
    {
        assumeTrue(hasInternet);
        mi.updateMirrors();
    }

    @Test
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
