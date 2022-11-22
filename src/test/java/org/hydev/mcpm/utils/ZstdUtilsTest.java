package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import static org.hydev.mcpm.utils.ZstdUtils.compress;
import static org.hydev.mcpm.utils.ZstdUtils.decompress;
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
        assumeTrue(ZstdUtils.isSupported());
        assertEquals(new String(decompress(compress("Hello world".getBytes()))), "Hello world");
        assertThrows(ZstdException.class, () -> decompress("not a zstd archive".getBytes()));
    }
}
