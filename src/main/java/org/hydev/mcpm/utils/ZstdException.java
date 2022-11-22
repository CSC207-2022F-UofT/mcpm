package org.hydev.mcpm.utils;

import java.io.IOException;

/**
 * Errors during the zstd compression/decompression
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-22
 */
public class ZstdException extends IOException
{
    public ZstdException(Throwable cause)
    {
        super(cause);
    }

    public ZstdException(String message)
    {
        super(message);
    }
}
