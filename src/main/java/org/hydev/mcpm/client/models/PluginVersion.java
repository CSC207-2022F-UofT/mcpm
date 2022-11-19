package org.hydev.mcpm.client.models;

/**
 * This class describes one version of a package that the package manager can download.
 *
 * @param size Size of compressed archive
 * @param sha256 SHA256 Hash Checksum (for validation)
 * @param meta Meta info stored in plugin.yml
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public record PluginVersion(
    long id,
    long size,
    String sha256,
    PluginYml meta
)
{
}
