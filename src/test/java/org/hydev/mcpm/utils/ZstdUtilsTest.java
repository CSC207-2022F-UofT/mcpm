package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

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
}
