package org.hydev.mcpm.client.database;

import java.util.List;

/**
 * Model for a mirror source
 *
 * @param host Hosting domain or half url of the mirror (e.g. "mcprs.hydev.org" or "eg.com/mcprs")
 * @param protocols Supported protocols. Can contain "http", "https", or "rsyncd"
 * @param tier Mirror tier. 0th tier mirrors are the source. 1st tier mirrors are synced from 0th tier mirrors, etc.
 * @param country Origin of the mirror (e.g. Canada)
 * @param speed Actual upstream speed in mbps, can be tested on speedtest.net
 * @param interval Interval in hours between updates
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-01
 */
public record Mirror(
    String host,
    List<String> protocols,
    int tier,
    String country,
    int speed,
    int interval
)
{
}
