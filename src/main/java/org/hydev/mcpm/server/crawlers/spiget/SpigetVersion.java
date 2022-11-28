package org.hydev.mcpm.server.crawlers.spiget;

/**
 * Spiget Version POJO for endpoint api.spiget.org/v2/resources/{resource_id}/versions
 *
 * @param uuid Unique ID for the version
 * @param id Numeric ID of the version
 */
public record SpigetVersion(
    String uuid,
    long id
)
{
}
