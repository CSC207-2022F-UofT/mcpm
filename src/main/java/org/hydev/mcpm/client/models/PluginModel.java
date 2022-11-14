package org.hydev.mcpm.client.models;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;


/**
 * This class describes one package.
 *
 * @param id Raw Plugin ID
 * @param versions Current & historical versions (sorted in time order)
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public record PluginModel(
    long id,
    List<PluginVersion> versions
)
{
    /**
     * Returns the latest version.
     *
     * @return The PluginVersion with the greatest id (apparently newer states take higher values?).
     */
    public Optional<PluginVersion> latest() {
        return versions().stream().max(Comparator.comparingLong(PluginVersion::id));
    }
}
