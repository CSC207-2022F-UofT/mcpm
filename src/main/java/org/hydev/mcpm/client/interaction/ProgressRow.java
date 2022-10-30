package org.hydev.mcpm.client.interaction;

import java.util.Objects;

import static java.lang.String.format;

/**
 * Row of a progress bar
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-30
 */
public class ProgressRow
{
    private final long total;
    private long completed;
    private final String unit;

    private long startTime;
    private ProgressBar pb;

    public ProgressRow(long total, String unit)
    {
        this.total = total;
        this.completed = 0;

        // Add leading space
        this.unit = (!unit.isBlank() && !unit.startsWith(" ")) ? " " + unit : unit;

        // Record start time for speed estimation
        this.startTime = System.nanoTime();
    }

    public ProgressRow(long total)
    {
        this(total, "it");
    }

    /**
     * Get formatted string of the current progress bar
     *
     * @param theme Progress bar theme
     * @param cols Number of columns (width) of the terminal window
     * @return Formatted string
     */
    public String fmt(ProgressBarTheme theme, int cols)
    {
        double p = 100d * completed / total;
        var placeholder = "PLACEHOLDER_BAR";
        var t = format("%s%s%s %3.0f%% %5d/%-5d%s", theme.prefix(), placeholder, theme.suffix(), p, completed, total, unit);

        // Add progress bar length
        var len = cols - t.length() + placeholder.length();

        // Calculate progress length
        int pLen = (int) (1d * completed / total * len);
        var bar = theme.done().repeat(pLen / theme.doneLen()) + theme.ipr().repeat((len - pLen) / theme.iprLen());

        return t.replaceFirst(placeholder, bar);
    }

    public void setPb(ProgressBar pb)
    {
        this.pb = pb;
    }

    /**
     * Increase progress
     *
     * @param incr Increase amount
     */
    public void increase(long incr)
    {
        this.completed += incr;
        pb.update();
        if (completed >= total) pb.finishBar(this);
    }

    /**
     * Set progress
     *
     * @param completed Completed amount
     */
    public void set(long completed)
    {
        this.completed = completed;
        pb.update();
        if (completed >= total) pb.finishBar(this);
    }
}
