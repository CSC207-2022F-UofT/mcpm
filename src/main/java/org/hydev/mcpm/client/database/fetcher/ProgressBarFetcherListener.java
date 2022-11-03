package org.hydev.mcpm.client.database.fetcher;

import org.hydev.mcpm.client.interaction.ProgressBar;
import org.hydev.mcpm.client.interaction.ProgressBarTheme;
import org.hydev.mcpm.client.interaction.ProgressRow;

/**
 * Handles database download events by forwarding them to a ProgressBar instance.
 */
public class ProgressBarFetcherListener implements DatabaseFetcherListener {
    private ProgressBar cachedBar;
    private ProgressRow cachedRow;

    private final ProgressBarTheme theme;

    /**
     * Create a ProgressBar with a default ASCII_THEME.
     */
    public ProgressBarFetcherListener() {
        this(ProgressBarTheme.ASCII_THEME);
    }

    /**
     * Create a ProgressBar with the provided them.
     *
     * @param theme The theme of the underlying ProgressBar.
     */
    public ProgressBarFetcherListener(ProgressBarTheme theme) {
        this.theme = theme;
    }

    // Writes to cachedBar/cachedRow...
    private void createBar(long total) {
        var bar = new ProgressBar(theme);
        var row = new ProgressRow(total)
            .desc("Database")
            .descLen(20)
            .unit(" bytes");

        bar.appendBar(row);

        cachedBar = bar;
        cachedRow = row;
    }

    private ProgressBar getBar(long total) {
        if (cachedBar == null) {
            createBar(total);
        }

        return cachedBar;
    }

    private ProgressRow getRow(long total) {
        if (cachedRow == null) {
            createBar(total);
        }

        return cachedRow;
    }

    @Override
    public void download(long completed, long total) {
        var row = getRow(total);

        row.set(completed);
    }

    @Override
    public void finish() {
        if (cachedBar != null) {
            cachedBar.getActiveBars().forEach(row -> cachedBar.finishBar(row));
            cachedBar.close();
        }
    }
}
