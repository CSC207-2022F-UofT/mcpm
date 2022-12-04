package org.hydev.mcpm.utils;

import com.google.common.base.Stopwatch;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hydev.mcpm.utils.ZstdUtils.compress;
import static org.hydev.mcpm.utils.ZstdUtils.compressPure;
import static org.hydev.mcpm.utils.ZstdUtils.decompress;
import static org.hydev.mcpm.utils.ZstdUtils.decompressPure;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for ZstdUtils
 */
class ZstdUtilsTest
{
    @Test
    void test() throws ZstdException
    {
        assumeTrue(ZstdUtils.nativeSupport());
        assertEquals(new String(decompress(compress("Hello world".getBytes(UTF_8))), UTF_8), "Hello world");
        assertEquals(new String(decompressPure(compress("Hello world".getBytes(UTF_8))), UTF_8), "Hello world");
        assertEquals(new String(decompress(compressPure("Hello world".getBytes(UTF_8))), UTF_8), "Hello world");
        assertEquals(new String(decompressPure(compressPure("Hello world".getBytes(UTF_8))), UTF_8), "Hello world");
        assertThrows(ZstdException.class, () -> decompress("not a zstd archive".getBytes(UTF_8)));
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
