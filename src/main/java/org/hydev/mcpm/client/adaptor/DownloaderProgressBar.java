package org.hydev.mcpm.client.adaptor;

import org.hydev.mcpm.client.interaction.ProgressBar;
import org.hydev.mcpm.client.interaction.ProgressBarTheme;
import org.hydev.mcpm.client.interaction.ProgressRowBoundary;

import java.util.List;

public class DownloaderProgressBar extends ProgressBar {
    /**
     * Create and initialize a progress bar
     *
     * @param theme Selected theme
     */
    public DownloaderProgressBar(ProgressBarTheme theme) {
        super(theme);

    }

    @Override
    public void incrementBarProgress(int id, long inc) {
        List<ProgressRowBoundary> bars = getBars();
        bars.get(id).increase(inc);
    }

}
