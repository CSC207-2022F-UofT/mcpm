package org.hydev.mcpm.utils;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.util.stream.IntStream;

/**
 * Network utilities
 */
public class NetworkUtils
{
    private static final Timeout ONE_S = Timeout.ofSeconds(1);
    private static final int PING_ITERS = 3;
    private static boolean pingInitialized = false;

    /**
     * Measure the ping (internet connectivity delay) to an url
     *
     * @param url Target url
     * @return Ping in milliseconds, or -1 if it cannot connect
     */
    public static int ping(String url)
    {
        // Preheat HTTP request module so that subsequent access return similar delay
        if (!pingInitialized)
        {
            try
            {
                Request.head("https://every1dns.com").connectTimeout(ONE_S).execute();
            }
            catch (IOException ignored) { }
            pingInitialized = true;
        }

        url = (url.startsWith("http://") || url.startsWith("https://")) ? url : "http://" + url;
        final var u = url;

        var start = System.currentTimeMillis();

        // Do request and check code
        var hasSuccess = IntStream.range(0, PING_ITERS).anyMatch(i ->
        {
            try
            {
                // Status code < 400 means HTTP success for HEAD (either 200 success or 300 redirect)
                return Request.head(u).connectTimeout(ONE_S).responseTimeout(ONE_S)
                    .execute().returnResponse().getCode() < 400;
            }
            catch (IOException e)
            {
                return false;
            }
        });

        if (!hasSuccess) return -1;

        // Return time
        return (int) (System.currentTimeMillis() - start) / PING_ITERS;
    }
}
