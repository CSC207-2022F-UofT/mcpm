package org.hydev.mcpm.server.crawlers.spiget;

/**
 * Spiget Version POJO for endpoint api.spiget.org/v2/resources/{resource_id}/versions
 *
 * @param downloads Number of downloads
 * @param url Link to download
 * @param name Version name (e.g. 1.0)
 * @param releaseDate Release timestamp in milliseconds
 * @param resource Resource ID (should be the same for all versions of one resource)
 * @param uuid Unique ID for the version
 * @param id Numeric ID of the version
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-28
 */
public record SpigetVersion(
    int downloads,
    String url,
    String name,
    long releaseDate,
    long resource,
    String uuid,
    long id
)
{
}
