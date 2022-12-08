package org.hydev.mcpm.server.spiget;

import java.util.List;
import java.util.Map;

/**
 * Spiget Resource POJO for endpoint api.spiget.org/v2/resources
 *
 * @param id Resource ID
 * @param name Displayed name (NOT UNIQUE/FINAL!)
 * @param tag Displayed description
 * @param external Whether it's an external resource
 * @param likes Number of likes
 * @param testedVersions Tested versions
 * @param links Links
 * @param releaseDate Release timestamp in ms
 * @param updateDate Update timestamp in ms
 * @param downloads Number of downloads
 * @param existenceStatus Existence status, IDK what this means
 * @param version Latest version ID and UUID
 */
@SuppressWarnings("unused")
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
    long existenceStatus,
    SpigetVersion version
)
{
}
