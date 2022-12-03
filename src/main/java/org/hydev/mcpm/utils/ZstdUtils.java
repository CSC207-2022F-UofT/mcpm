package org.hydev.mcpm.utils;

import io.airlift.compress.MalformedInputException;
import io.airlift.compress.zstd.ZstdCompressor;
import io.airlift.compress.zstd.ZstdDecompressor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Utility for Zstd-jni
 */
public class ZstdUtils
{
    public static final ReflectedZstd zstd = getZstdClass();

    /**
     * Reflected zstd class
     *
     * @param cl Class object
     * @param decompress decompress(byte[], int) method
     * @param decompressedSize decompressedSize(byte[]) method
     * @param compress compress(byte[]) method
     */
    public record ReflectedZstd(Class<?> cl, Method decompress, Method decompressedSize, Method compress) { }

    /**
     * Get zstd class
     *
     * @return Zstd class if usable, null otherwise
     */
    private static @Nullable ReflectedZstd getZstdClass()
    {
        try
        {
            var cl = Class.forName("com.github.luben.zstd.Zstd");
            var decompress = cl.getMethod("decompress", byte[].class, int.class);
            var decompressedSize = cl.getMethod("decompressedSize", byte[].class);
            var compress = cl.getMethod("compress", byte[].class, int.class);
            return new ReflectedZstd(cl, decompress, decompressedSize, compress);
        }
        catch (Throwable e)
        {
            // Any exception here would mean that it isn't supported
            return null;
        }
    }

    /**
     * Check if zstd is supported on the current platform
     *
     * @return true if supported
     */
    public static boolean nativeSupport()
    {
        return zstd != null;
    }

    /**
     * Compress zstd using reflection, with default compression level 8
     *
     * @param content Plain text content
     * @return Zstd compressed content
     */
    public static byte[] compress(byte[] content) throws ZstdException
    {
        return compress(content, 8);
    }

    /**
     * Compress zstd using reflection
     *
     * @param content Plain text content
     * @param level Compression level
     * @return Zstd compressed content
     */
    public static byte[] compress(byte[] content, int level) throws ZstdException
    {
        try
        {
            // !nativeSupport()
            if (!nativeSupport()) {
                return compressPure(content);
            }

            // nativeSupport checks this for us, we want to silence the warning
            assert zstd != null;

            //noinspection RedundantCast
            return (byte[]) zstd.compress.invoke(null, (Object) content, level);
        }
        catch (IllegalAccessException e)
        {
            // This would never happen
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException | MalformedInputException e)
        {
            // This would be when zstd has some errors
            throw new ZstdException(e);
        }
    }

    /**
     * Compress zstd using reflection, with default compression level 8
     *
     * @param content Plain text content
     * @return Zstd compressed content
     */
    public static byte[] compressPure(byte[] content)
    {
        var zc = new ZstdCompressor();
        var maxDstSize = zc.maxCompressedLength(content.length);
        byte[] dst = new byte[(int) maxDstSize];

        // TODO: Compression level
        // ZstdCompressor doesn't have a compression level argument
        // ZstdFrameCompressor which ZstdCompressor uses does, but it isn't public
        int size = zc.compress(content, 0, content.length, dst, 0, dst.length);
        return Arrays.copyOfRange(dst, 0, size);
    }

    /**
     * Decompress zstd using reflection
     *
     * @param zst Zstd compressed content
     * @return Plain text content
     */
    public static byte[] decompress(byte[] zst) throws ZstdException
    {
        try
        {
            if (!nativeSupport()) {
                return decompressPure(zst);
            }

            // nativeSupport checks this for us, we want to silence the warning
            assert zstd != null;

            //noinspection RedundantCast
            return (byte[]) zstd.decompress.invoke(null, zst,
                ((Long) zstd.decompressedSize.invoke(null, (Object) zst)).intValue());
        }
        catch (IllegalAccessException e)
        {
            // This would never happen
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException | MalformedInputException e)
        {
            // This would be when zstd has some errors
            throw new ZstdException(e);
        }
    }

    /**
     * Decompress with pure-java implementation io.airlift.aircompressor to support platforms that doesn't support
     * the native JNI library.
     *
     * @param zst Zstd compressed content
     * @return Plain text content
     */
    public static byte[] decompressPure(byte[] zst)
    {
        var originalSize = ZstdDecompressor.getDecompressedSize(zst, 0, zst.length);

        byte[] dst = new byte[(int) originalSize];
        int size = new ZstdDecompressor().decompress(zst, 0, zst.length, dst, 0, (int) originalSize);
        if (size != originalSize) {
            return Arrays.copyOfRange(dst, 0, size);
        } else {
            return dst;
        }
    }

    /**
     * Main method for the zstd test
     *
     * @param args Args (not used)
     */
    public static void main(String[] args) throws ZstdException
    {
        System.out.printf("Detected ZSTD support: %s\n", ZstdUtils.nativeSupport());

        var cb = ZstdUtils.compress("Zstd compression/decompression works!".getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(ZstdUtils.decompress(cb), StandardCharsets.UTF_8));
    }
}
