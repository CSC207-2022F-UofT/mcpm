package org.hydev.mcpm.client.interaction;

import static java.lang.String.format;

/**
 * Row of a progress bar
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-30
 */
public class ProgressRow
{
    protected long total;
    protected long completed;
    protected String unit;

    public ProgressRow(long total, String unit)
    {
        this.total = total;
        this.completed = 0;
        this.unit = unit;
    }

    public ProgressRow(long total)
    {
        this(total, "it");
    }

    /**
     * Get formatted string of the current progress bar
     *
     * @param theme Progress bar theme
     * @return Formatted string
     */
    public String fmt(ProgressBarTheme theme)
    {
        double p = 100d * completed / total;
        var placeholder = "PLACEHOLDER_BAR";
        var t = format("%s%s%s %.0f %d/%d%s", theme.prefix(), placeholder, theme.suffix(), p, completed, total, unit);

        // Add progress bar length
        var len = t.length() - placeholder.length();

        // Calculate progress length
        int pLen = (int) (completed / total * len);
        var bar = theme.done().repeat(pLen / theme.doneLen()) + theme.ipr().repeat(pLen / theme.iprLen());

        return t.replaceFirst(placeholder, bar);
    }
}
