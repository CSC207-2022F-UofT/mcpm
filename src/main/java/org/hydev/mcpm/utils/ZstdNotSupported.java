package org.hydev.mcpm.utils;

/**
 * Zstd is not supported on the current platform
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-22
 */
public class ZstdNotSupported extends ZstdException
{
    public ZstdNotSupported()
    {
        super("Zstd is not supported on the current platform.");
    }
}
