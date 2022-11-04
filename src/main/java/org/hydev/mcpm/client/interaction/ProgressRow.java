package org.hydev.mcpm.client.interaction;

/**
 * Row of a progress bar
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-30
 */
public class ProgressRow implements ProgressRowBoundary {
    private final long total;
    private final int id;
    private long completed;
    private String unit;
    private ProgressBarBoundary pb;
    private String desc;
    private int descLen;
    private String fmt;

    private final long startTime;

    /**
     * Creates a ProgressRow object with a maximum 100% value of total.
     *
     * @param total The value that completed has to take to display 100% in this row.
     */
    public ProgressRow(long total, int id)
    {
        this.total = total;
        this.id = id;
        this.completed = 0;
        this.unit = " it";
        this.desc = "";
        this.descLen = 20;
        this.fmt = "{desc}{speed} {eta} {prefix}{progbar}{suffix} {%done}";

        // Record start time for speed estimation
        this.startTime = System.nanoTime();
    }

    public int getId(){
        return this.id;
    }

    /**
     * @return Elapsed time in seconds
     */
    private double elapsed()
    {
        return (System.nanoTime() - startTime) / 1e9d;
    }

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
    public void setPb(ProgressBarBoundary pb)
    {
        this.pb = pb;
    }

    @Override
    public void increase(long incr)
    {
        if (this.completed >= total) return;

        this.completed += incr;
        this.completed = Math.min(total, this.completed); // We don't want to go over
        pb.update();
        if (completed >= total) pb.finishBar(this.id);
    }

    @Override
    public void set(long completed)
    {
        if (this.completed >= total) return;

        this.completed = completed;
        pb.update();
        if (completed >= total) pb.finishBar(this.id);
    }

    @Override
    public ProgressRowBoundary desc(String desc)
    {
        this.desc = desc;
        return this;
    }

    @Override
    public ProgressRowBoundary descLen(int descLen)
    {
        this.descLen = descLen;
        return this;
    }

    @Override
    public ProgressRowBoundary unit(String unit)
    {
        // Add leading space
        this.unit = (!unit.isBlank() && !unit.startsWith(" ")) ? " " + unit : unit;
        return this;
    }

    @Override
    public ProgressRowBoundary format(String fmt)
    {
        this.fmt = fmt;
        return this;
    }
}
