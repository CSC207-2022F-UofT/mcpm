package org.hydev.mcpm.client.interaction;

import java.util.List;

/**
 * Terminal progress bar based on Xterm escape codes
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

    /**
     * Return a list of ProgressRowBoundary that this ProgressBarBoundary uses
     *
     * @return the bars attached to this object
     */
    List<ProgressRowBoundary> getBars();
}
