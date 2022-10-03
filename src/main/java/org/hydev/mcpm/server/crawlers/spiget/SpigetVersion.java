package org.hydev.mcpm.server.crawlers.spiget;

/**
 * Spiget Version POJO for endpoint api.spiget.org/v2/resources/{resource_id}/versions
 *
 * @param uuid Unique ID for the version
 * @param id Numeric ID of the version
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-28
 */
public record SpigetVersion(
    String uuid,
    long id
)
{
}
