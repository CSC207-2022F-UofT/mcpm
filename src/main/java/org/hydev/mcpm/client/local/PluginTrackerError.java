package org.hydev.mcpm.client.local;

/**
 * Exception class for the local plugin tracker
 */
public class PluginTrackerError extends RuntimeException
{
    public PluginTrackerError()
    {
    }

    public PluginTrackerError(String message)
    {
        super(message);
    }

    public PluginTrackerError(String message, Throwable cause)
    {
        super(message, cause);
    }

    public PluginTrackerError(Throwable cause)
    {
        super(cause);
    }

    public PluginTrackerError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
