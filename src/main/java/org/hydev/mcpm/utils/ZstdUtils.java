package org.hydev.mcpm.utils;

import org.jetbrains.annotations.Nullable;

import java.awt.color.ICC_ProfileGray;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Ref;

/**
 * Utility for Zstd-jni
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-22
 */
public class ZstdUtils
{
    public static final ReflectedZstd zstd;

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
    private static @Nullable ReflectedZstd _getZstdClass()
    {
        try
        {
            var cl = Class.forName("com.github.luben.zstd.Zstd");
            var decompress = cl.getMethod("decompress", byte[].class, int.class);
            var decompressedSize = cl.getMethod("decompressedSize", byte[].class);
            var compress = cl.getMethod("compress", byte[].class);
            return new ReflectedZstd(cl, decompress, decompressedSize, compress);
        }
        catch (Exception e)
        {
            // Any exception here would mean that it isn't supported
            return null;
        }
    }

    static
    {
        zstd = _getZstdClass();
    }

    /**
     * Check if zstd is supported on the current platform
     *
     * @return true if supported
     */
    public static boolean isSupported()
    {
        return zstd != null;
    }

    /**
     * Compress zstd using reflection
     *
     * @param content Plain text content
     * @return Zstd compressed content
     */
    public static byte[] compress(byte[] content) throws ZstdException
    {
        if (!isSupported()) throw new ZstdNotSupported();
        try
        {
            return (byte[]) zstd.compress.invoke(null, (Object) content);
        }
        catch (IllegalAccessException e)
        {
            // This would never happen
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            // This would be when zstd has some errors
            throw new ZstdException(e);
        }
    }

    /**
     * Decompress zstd using reflection
     *
     * @param zst Zstd compressed content
     * @return Plain text content
     */
    public static byte[] decompress(byte[] zst) throws ZstdException
    {
        if (!isSupported()) throw new ZstdNotSupported();
        try
        {
            return (byte[]) zstd.decompress.invoke(null, zst,
                ((Long) zstd.decompressedSize.invoke(null, (Object) zst)).intValue());
        }
        catch (IllegalAccessException e)
        {
            // This would never happen
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            // This would be when zstd has some errors
            throw new ZstdException(e);
        }
    }
}
