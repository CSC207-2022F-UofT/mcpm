package org.hydev.mcpm.client.display;

/**
 * A ProgressSpeedBoundary that also supports getting speed for variable windows.
 */
public interface VariableWindowProgressSpeedBoundary extends ProgressSpeedBoundary {
    /**
     * Get the speed of the ProgressRowBoundary.
     *
     * @param window time window over which to measure speed.
     * @return speed of the ProgressRowBoundary over the window.
     */
    double getSpeed(long window);
}
