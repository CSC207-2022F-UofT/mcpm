package org.hydev.mcpm.utils;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import static org.hydev.mcpm.utils.ZstdUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for ZstdUtils
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-22
 */
class ZstdUtilsTest
{
    @Test
    void test() throws ZstdException
    {
        assumeTrue(ZstdUtils.nativeSupport());
        assertEquals(new String(decompress(compress("Hello world".getBytes()))), "Hello world");
        assertEquals(new String(decompressPure(compress("Hello world".getBytes()))), "Hello world");
        assertEquals(new String(decompress(compressPure("Hello world".getBytes()))), "Hello world");
        assertEquals(new String(decompressPure(compressPure("Hello world".getBytes()))), "Hello world");
        assertThrows(ZstdException.class, () -> decompress("not a zstd archive".getBytes()));
    }

    /**
     * Speed test
     *
     * @param args Not used
     */
    public static void main(String[] args) throws IOException
    {
        var zf = GeneralUtils.getResourceFile("db.zst");
        assert zf != null;
        var zb = Files.readAllBytes(zf.toPath());

        var n = 100;
        var time = Stopwatch.createStarted();
        for (int i = 0; i < n; i++)
        {
            ZstdUtils.decompress(zb);
        }
        System.out.printf("Native Time: %s ms\n", time.elapsed(TimeUnit.MILLISECONDS));

        time.reset();
        time.start();
        for (int i = 0; i < n; i++)
        {
            ZstdUtils.decompressPure(zb);
        }
        System.out.printf("Pure Time: %s ms\n", time.elapsed(TimeUnit.MILLISECONDS));
    }
}
