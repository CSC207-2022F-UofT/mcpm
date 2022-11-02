package org.hydev.mcpm.client.database;

import org.hydev.mcpm.utils.Pair;
import org.jetbrains.annotations.NotNull;

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
     * Measure the ping (internet connectivity delay) of mirrors
     *
     * @return Sorted list of mirrors with their latencies, with the fastest on top
     */
    List<Pair<Mirror, Integer>> pingMirrors() throws IOException;
}
