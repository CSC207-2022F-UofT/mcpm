package org.hydev.mcpm.client.database.mirrors;

import org.hydev.mcpm.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Comparator.comparingInt;
import static org.hydev.mcpm.utils.NetworkUtils.ping;

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
     * <p>This is implemented as a default interface method because different implementations of
     * MirrorSelectBoundary shouldn't have different implementations of this method. Check out our
     * discussion thread https://github.com/CSC207-2022F-UofT/mcpm/pull/15#discussion_r1012044200
     * for more details.
     *
     * @return Sorted list of mirrors with their latencies, with the fastest on top
     */
    default List<Pair<Mirror, Integer>> pingMirrors() throws IOException
    {
        return listAvailableMirrors().stream().filter(Mirror::isWeb)
            .parallel()
            .map(m -> new Pair<>(m, ping(m.url())))
            .sorted(comparingInt(Map.Entry::getValue))
            .toList();
    }

    /**
     * Get the mirror that the user has selected, or the default mirror
     *
     * @return A selected mirror
     */
    Mirror getSelectedMirror() throws IOException;

    /**
     * Set a mirror as user-selected
     *
     * @param mirror A selected mirror
     */
    void setSelectedMirror(Mirror mirror) throws IOException;

    /**
     * Get the selected mirror as a URI supplier
     *
     * @return URI supplier function
     */
    default Supplier<URI> selectedMirrorSupplier()
    {
        return () ->
        {
            try
            {
                return getSelectedMirror().uri();
            }
            catch (IOException e)
            {
                // Fallback
                e.printStackTrace();
                return URI.create("https://mcpm.hydev.org/");
            }
        };
    }
}
