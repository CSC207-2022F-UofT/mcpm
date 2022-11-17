package org.hydev.mcpm.client.interaction;

/**
 * Terminal progress bar based on Xterm escape codes
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @author Peter (https://github.com/MstrPikachu)
 * @since 2022-09-27
 */
public interface ProgressBarBoundary extends AutoCloseable {
    /**
     * Append a progress bar at the end
     *
     * @param bar Row of the progress bar
     * @return bar for fluent access
     */
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

    ProgressBar setFrameDelay(double frameDelay);

    /**
     * Set frame rate in the unit of frames per second
     *
     * @param fps FPS
     * @return Self for fluent access
     */
    ProgressBar setFps(int fps);
}
