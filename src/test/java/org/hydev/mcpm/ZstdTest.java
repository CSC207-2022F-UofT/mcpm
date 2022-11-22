package org.hydev.mcpm;

import com.github.luben.zstd.Zstd;

/**
 * This is not a unit test, but a manual test to see if your platform is supported by Zstd-jni.
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-22
 */
public class ZstdTest
{
    public static void main(String[] args)
    {
        var enableCompression = false;
        try
        {
            Zstd.compress("Hello world".getBytes());
            enableCompression = true;
        }
        catch (Exception ignored) { }
        System.out.printf("Detected ZSTD support: %s\n", enableCompression);

        var cb = Zstd.compress("Zstd compression/decompression works!".getBytes());
        System.out.println(new String(Zstd.decompress(cb, (int) Zstd.decompressedSize(cb))));
    }
}
