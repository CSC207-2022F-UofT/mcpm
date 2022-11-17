package org.hydev.mcpm.client.interaction;

/**
 * Row of a progress bar
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-30
 */
public class ProgressRow implements ProgressRowBoundary {
    private final long total;
    private long completed;
    private String unit;
    private ProgressBar pb;
    private String desc;
    private int descLen;
    private String fmt;

    private final long startTime;

    /**
     * Creates a ProgressRow object with a maximum 100% value of total.
     *
     * @param total The value that completed has to take to display 100% in this row.
     */
    public ProgressRow(long total)
    {
        this.total = total;
        this.completed = 0;
        this.unit = " it";
        this.desc = "";
        this.descLen = 20;
        this.fmt = "{desc}{speed} {eta} {prefix}{progbar}{suffix} {%done}";

        // Record start time for speed estimation
        this.startTime = System.nanoTime();
    }

    /**
     * @return Elapsed time in seconds
     */
    private double elapsed()
    {
        return (System.nanoTime() - startTime) / 1e9d;
    }

    /**
     * Get formatted string of the current progress bar
     *
     * @param theme Progress bar theme
     * @param cols Number of columns (width) of the terminal window
     * @return Formatted string
     */
    @Override
    public String toString(ProgressBarTheme theme, int cols)
    {
        // Calculate speed. TODO: Use a moving window to calculate speed
        double speed = completed / elapsed();
        double eta = total / speed;
        long etaS = (long) (eta % 60);
        long etaM = (long) (eta / 60);

        // Replace variables
        var p = String.format("%3.0f%%", 100d * completed / total);
        var t = fmt.replace("{prefix}", theme.prefix())
            .replace("{suffix}", theme.suffix())
            .replace("{%done}", p)
            .replace("{eta}", String.format("%02d:%02d", etaM, etaS))
            .replace("{speed}", String.format("%.2f%s/s", speed, unit))
            .replace("{desc}", descLen != 0 ? String.format("%-" + descLen + "s", desc) : desc + " ");

        // Add progress bar length
        var len = cols - t.length() + "{progbar}".length();

        // Safety check: If there are no space to display the progress bar, then don't display it
        if (len < 0) return t.replace("{progbar}", "");

        // Calculate progress length
        int progress = (int) (1d * completed / total * len);

        var leading = theme.done().repeat(progress / theme.doneLen());
        var trailing = theme.ipr().repeat((len - progress) / theme.iprLen());

        var bar = leading + trailing;

        return t.replace("{progbar}", bar);
    }

    @Override
    public void setPb(ProgressBar pb)
    {
        this.pb = pb;
    }

    /**
     * Increase progress by incr.
     *
     * @param incr Increase amount
     */
    @Override
    public void increase(long incr)
    {
        if (this.completed >= total) return;

        this.completed += incr;
        this.completed = Math.min(total, this.completed); // We don't want to go over
        pb.update();
        if (completed >= total) pb.finishBar(this);
    }

    /**
     * Update the progress.
     *
     * @param completed Completed so far
     */
    @Override
    public void set(long completed)
    {
        if (this.completed >= total) return;

        this.completed = completed;
        pb.update();
        if (completed >= total) pb.finishBar(this);
    }

    /**
     * Sets the description of this object.
     *
     * @param desc The description to set
     * @return This object for chaining.
     */
    @Override
    public ProgressRow desc(String desc)
    {
        this.desc = desc;
        return this;
    }

    /**
     * Sets the maximum description length. This value is used for padding.
     *
     * @param descLen The description length.
     * @return This object for chaining.
     */
    @Override
    public ProgressRow descLen(int descLen)
    {
        this.descLen = descLen;
        return this;
    }

    /**
     * Sets the unit indicator for the speed indicator.
     *
     * @param unit A unit string, beginning with an empty space.
     * @return This object for chaining.
     */
    @Override
    public ProgressRow unit(String unit)
    {
        // Add leading space
        this.unit = (!unit.isBlank() && !unit.startsWith(" ")) ? " " + unit : unit;
        return this;
    }

    /**
     * Sets the overall format of the progress bar.
     * This is specified using indicators like {speed}, {desc}.
     * For a full list see class implementation.
     *
     * @param fmt The foramt string.
     * @return This object for chaining.
     */
    @Override
    public ProgressRow fmt(String fmt)
    {
        this.fmt = fmt;
        return this;
    }
}
