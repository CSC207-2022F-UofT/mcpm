package org.hydev.mcpm.utils;

import java.io.IOException;

/**
 * Errors during the zstd compression/decompression
 */
public class ZstdException extends IOException
{
    public ZstdException(Throwable cause)
    {
        super(cause);
    }
}
