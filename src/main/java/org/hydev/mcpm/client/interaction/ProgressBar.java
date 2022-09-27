package org.hydev.mcpm.client.interaction;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class ProgressBar implements AutoCloseable
{
    private final ProgressBarTheme theme;

    /**
     * Create and initialize a progress bar
     *
     * @param theme Selected theme
     */
    public ProgressBar(ProgressBarTheme theme)
    {
        this.theme = theme;
        this.init();
    }

    /**
     * Initialize the progress bar (print the first line)
     */
    public void init()
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Append a progress bar at the end
     *
     * @return Unique identifier of the progress bar
     */
    public String appendBar()
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Set progress for a bar
     *
     * @param id Unique identifier
     * @param progress Progress as a ratio in range 0-1
     */
    public void setBar(String id, float progress)
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Finish a progress bar
     *
     * @param id Unique identifier of the progress bar
     */
    public void finishBar(String id)
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Finalize and close the progress bar (print the final line)
     *
     * @throws Exception e
     */
    @Override
    public void close() throws Exception
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }
}
