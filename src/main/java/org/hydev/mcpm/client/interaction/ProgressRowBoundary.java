package org.hydev.mcpm.client.interaction;

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

    void setPb(ProgressBar pb);

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
     * Sets the description of this object.
     *
     * @param desc The description to set
     * @return This object for chaining.
     */
    ProgressRowBoundary desc(String desc);

    /**
     * Sets the maximum description length. This value is used for padding.
     *
     * @param descLen The description length.
     * @return This object for chaining.
     */
    ProgressRowBoundary descLen(int descLen);

    /**
     * Sets the unit indicator for the speed indicator.
     *
     * @param unit A unit string, beginning with an empty space.
     * @return This object for chaining.
     */
    ProgressRowBoundary unit(String unit);

    /**
     * Sets the overall format of the progress bar.
     * This is specified using indicators like {speed}, {desc}.
     * For a full list see class implementation.
     *
     * @param fmt The format string.
     * @return This object for chaining.
     */
    ProgressRowBoundary fmt(String fmt);
}
