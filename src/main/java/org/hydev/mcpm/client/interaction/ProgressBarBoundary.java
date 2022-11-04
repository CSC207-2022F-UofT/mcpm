package org.hydev.mcpm.client.interaction;
public interface ProgressBarBoundary extends AutoCloseable {
    /**
     * Append a progress bar at the end
     *
     * @param total The amount of progress bar needs to reach to be full
     * @return bar for fluent access
     */
    ProgressRowBoundary appendBar(long total);

    /**
     * Increment the progress of the bar with id by a value
     * @param id The id of the progress bar
     * @param inc The value to the progress increment by
     */
    void incrementBarProgress(int id, long inc);

    /**
     * Set the progress of a progress bar to a value
     * @param id The id of the progress bar
     * @param progress The value to set the progress to
     */
    void setBarProgress(int id, long progress);

    void update();

    /**
     * Finish a progress bar
     *
     * @param id the id of the Progress bar
     */
    void finishBar(int id);

    ProgressBarBoundary setFrameDelay(double frameDelay);

    /**
     * Set frame rate in the unit of frames per second
     *
     * @param fps FPS
     * @return Self for fluent access
     */
    ProgressBarBoundary setFps(int fps);
}
