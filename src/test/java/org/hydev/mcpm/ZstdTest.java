package org.hydev.mcpm;

import org.hydev.mcpm.utils.ZstdException;
import org.hydev.mcpm.utils.ZstdUtils;

/**
 * This is not a unit test, but a manual test to see if your platform is supported by Zstd-jni.
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-22
 */
public class ZstdTest
{
    /**
     * Main method for the zstd test
     *
     * @param args Args (not used)
     */
    public static void main(String[] args) throws ZstdException
    {
        System.out.printf("Detected ZSTD support: %s\n", ZstdUtils.isSupported());

        var cb = ZstdUtils.compress("Zstd compression/decompression works!".getBytes());
        System.out.println(new String(ZstdUtils.decompress(cb)));
    }
}
