package org.hydev.mcpm.server.crawlers.spiget;

import java.util.List;
import java.util.Map;

/**
 * Spiget Resource POJO for endpoint api.spiget.org/v2/resources
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public record SpigetResource(
    long id,
    String name,
    String tag,
    boolean external,
    long likes,
    List<String> testedVersions,
    Map<String, String> links,
    long releaseDate,
    long updateDate,
    long downloads,
    long existenceStatus
)
{
}
