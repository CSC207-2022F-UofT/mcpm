package org.hydev.mcpm.client.display;

import org.hydev.mcpm.client.display.progress.ProgressBarTheme;

/**
 * Row of a progress bar.
 */
public interface ProgressRowBoundary {
    /**
     * Get formatted string of the current progress bar
     *
     * @param theme Progress bar theme
     * @param cols Number of columns (width) of the terminal window
     * @return Formatted string
     */
    String toString(ProgressBarTheme theme, int cols);

    /**
     * Increase progress by incr.
     *
     * @param incr Increase amount
     */
    void increase(long incr);

    /**
     * Update the progress.
     *
     * @param completed Completed so far
     */
    void set(long completed);

    /**
     * Get how much progress this row has currently completed as a fraction out of 1.
     *
     * @return the completion of this row.
     */
    double getCompletion();

    /**
     * Set the current progress bar for this row.
     *
     * @param bar The bar to set as the row's parent.
     */
    void setPb(ProgressBarBoundary bar);
}
