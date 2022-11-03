package org.hydev.mcpm.client.database.fetcher;

import org.hydev.mcpm.client.interaction.ProgressBar;
import org.hydev.mcpm.client.interaction.ProgressBarTheme;
import org.hydev.mcpm.client.interaction.ProgressRow;

public class ProgressBarFetcherListener implements DatabaseFetcherListener {
    private ProgressBar cachedBar;
    private ProgressRow cachedRow;

    private final ProgressBarTheme theme;

    public ProgressBarFetcherListener() {
        this(ProgressBarTheme.ASCII_THEME);
    }

    public ProgressBarFetcherListener(ProgressBarTheme theme) {
        this.theme = theme;
    }

    // Writes to cachedBar/cachedRow...
    void createBar(long total) {
        var bar = new ProgressBar(theme);
        var row = new ProgressRow(total)
            .desc("Database")
            .descLen(20)
            .unit(" bytes");

        bar.appendBar(row);

        cachedBar = bar;
        cachedRow = row;
    }

    ProgressBar getBar(long total) {
        if (cachedBar == null) {
            createBar(total);
        }

        return cachedBar;
    }

    ProgressRow getRow(long total) {
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
