package org.hydev.mcpm.client.models;

import java.util.List;


/**
 * This class describes one package.
 *
 * @param name Name of the package
 * @param desc Description of what the package does
 * @param url Home page URL
 * @param license License
 * @param maintainer Maintainer of the package
 * @param versions Current & historical versions (sorted in time order)
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public record Package(
    String name,
    String desc,
    String url,
    String license,
    String maintainer,
    List<PackageVersion> versions
)
{
}
