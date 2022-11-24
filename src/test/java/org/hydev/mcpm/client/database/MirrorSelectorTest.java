package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.mirrors.MirrorSelector;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hydev.mcpm.utils.NetworkUtils.ping;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-01
 */
class MirrorSelectorTest
{
    static MirrorSelector mi = new MirrorSelector();
    static boolean hasInternet = ping(mi.mirrorListUrl()) != -1;

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
            assert pings.get(i).v() <= pings.get(i + 1).v();
        }
    }
}
