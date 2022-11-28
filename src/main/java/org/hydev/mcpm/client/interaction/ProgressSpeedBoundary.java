package org.hydev.mcpm.client.interaction;

/**
 * Defines an interface for a class that can calculate the speed of a ProgressRowBoundary using the last x seconds.
 */
public interface ProgressSpeedBoundary {
    /**
     * Get the speed of the ProgressRowBoundary.
     *
     * @return the speed of the ProgressRowBoundary
     */
    double getSpeed();

    /**
     * Whenever the associated ProgressRowBoundary has setProgress called, call this as well.
     *
     * @param progress the updated progress
     */
    void setProgress(long progress);

    /**
     * Whenever the associated ProgressRowBoundary has incProgress called, call this as well.
     *
     * @param inc the increment of the progress
     */
    void incProgress(long inc);
}
