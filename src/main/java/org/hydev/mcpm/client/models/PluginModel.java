package org.hydev.mcpm.client.models;

import java.util.List;


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
}
