package org.hydev.mcpm.client.interaction;

import java.util.List;

public interface ProgressBarBoundary {
    ProgressRowBoundary appendBar(ProgressRowBoundary bar);

    void update();

    void finishBar(ProgressRowBoundary bar);

    ProgressBarBoundary setFrameDelay(double frameDelay);

    ProgressBarBoundary setFps(int fps);

    List<ProgressRowBoundary> getActiveBars();
}
