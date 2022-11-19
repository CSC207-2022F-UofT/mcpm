package org.hydev.mcpm.client.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 * @since 2022-09-27
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PluginModel(
    long id,
    List<PluginVersion> versions)
{
    /**
     * Gets the latest PluginVersion from all PluginVersions of itself.
     *
     * @return The latest PluginVersion of itself, if it exists.
     */
    @JsonIgnore
    public Optional<PluginVersion> getLatestPluginVersion() {
        return versions.stream().max(Comparator.comparingLong(PluginVersion::id));
    }
}
