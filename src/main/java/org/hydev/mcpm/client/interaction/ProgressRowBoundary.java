package org.hydev.mcpm.client.interaction;

/**
 * Row of a progress bar
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @author Peter (https://github.com/MstrPikachu)
 * @since 2022-10-30
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
     * @param fmt The foramt string.
     * @return This object for chaining.
     */
    ProgressRowBoundary fmt(String fmt);
}
