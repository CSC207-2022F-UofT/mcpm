package org.hydev.mcpm.client.database.mirrors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Model for a mirror source
 *
 * @param host Hosting domain of the mirror (e.g. "mcprs.hydev.org")
 * @param protocols Supported protocols. Can contain "http", "https", or "rsyncd"
 * @param tier Mirror tier. 0th tier mirrors are the source. 1st tier mirrors are synced from 0th tier mirrors, etc.
 * @param country Origin of the mirror (e.g. Canada)
 * @param speed Actual upstream speed in mbps, can be tested on speedtest.net
 * @param interval Interval in hours between updates
 * @param httpEndpoint Endpoint to the http file server (e.g. "abc" means "mcprs.hydev.org/abc")
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-01
 */
public record Mirror(
    String host,
    Set<String> protocols,
    int tier,
    String country,
    int speed,
    int interval,

    // TODO: Add defaults value when parsing for httpEndpoint
    String httpEndpoint
)
{
    /**
     * Check if the mirror supoorts either http or https.
     *
     * @return Supports web
     */
    @JsonIgnore
    public boolean isWeb()
    {
        return protocols.contains("http") || protocols.contains("https");
    }

    /**
     * Get the url of the mirror. Set to use https in priority.
     *
     * @return Http URL or null if it doesn't support http protocols
     */
    public @Nullable String url()
    {
        if (protocols.contains("https")) return "https://" + host + httpEndpoint();
        else if (protocols.contains("http")) return "http://" + host + httpEndpoint();
        else return null;
    }

    @Override
    public String toString()
    {
        return host;
    }

    @Override @JsonIgnore
    public String httpEndpoint()
    {
        if (httpEndpoint == null) return "/";

        // Ensure leading /
        return httpEndpoint.startsWith("/") ? httpEndpoint : "/" + httpEndpoint;
    }
}
