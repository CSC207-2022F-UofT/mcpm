package org.hydev.mcpm.client.database;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

/**
 * Interface for selecting a mirror
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-01
 */
public interface MirrorSelectBoundary
{
    /**
     * Automatically select a mirror
     *
     * @return Fastest mirror, or null if none are available
     */
    @Nullable
    Mirror selectMirror() throws IOException;

    /**
     * List available mirrors
     *
     * @return List of mirrors (can be cached)
     */
    @NotNull
    List<Mirror> listAvailableMirrors() throws IOException;

    /**
     * Update the list of available mirrors from a source.
     */
    void updateMirrors() throws IOException;

    /**
     * Measure the ping (internet connectivity delay) of a mirror
     *
     * @param mirror Mirror
     * @return Delay in milliseconds
     */
    int pingMirror(Mirror mirror);
}
