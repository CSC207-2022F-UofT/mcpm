package org.hydev.mcpm.client.models;

import java.util.List;


/**
 * This class describes one package.
 *
 * @param name Name of the package
 * @param desc Description of what the package does
 * @param url Home page URL (provided by the latest version's meta)
 * @param authors Authors (provided by the latest version's meta)
 * @param license License
 * @param maintainer Maintainer of the package
 * @param versions Current & historical versions (sorted in time order)
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public record PluginModel(
    String name,
    String desc,
    String url,
    List<String> authors,
    String license,
    String maintainer,
    List<PluginVersion> versions
)
{
}
