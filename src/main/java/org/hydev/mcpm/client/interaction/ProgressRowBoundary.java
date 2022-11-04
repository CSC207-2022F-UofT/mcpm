package org.hydev.mcpm.client.interaction;

public interface ProgressRowBoundary {
    String toString(ProgressBarTheme theme, int cols);

    void setPb(ProgressBarBoundary pb);

    void increase(long incr);

    void set(long completed);

    ProgressRowBoundary desc(String desc);

    ProgressRowBoundary descLen(int descLen);

    ProgressRowBoundary unit(String unit);

    ProgressRowBoundary format(String fmt);
}
