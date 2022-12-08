package org.hydev.mcpm.client.display;

/**
 * Terminal progress bar based on Xterm escape codes
 */
@SuppressWarnings("unused")
public interface ProgressBarBoundary extends AutoCloseable {
    /**
     * Append a progress bar at the end
     *
     * @param bar Row of the progress bar
     * @return bar for fluent access
     */
    @SuppressWarnings("UnusedReturnValue")
    ProgressRowBoundary appendBar(ProgressRowBoundary bar);

    /**
     * Finish a progress bar
     *
     * @param bar Progress bar
     */
    void finishBar(ProgressRowBoundary bar);

    /**
     * Finalize and close the progress bar (print the final line)
     */
    @Override
    void close();

    /**
     * Update the actual progress bar string (write to output).
     */
    void update();
}
