package org.hydev.mcpm.utils;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;

/**
 * Network utilities
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-01
 */
public class NetworkUtils
{
    private static final Timeout ONE_S = Timeout.ofSeconds(1);

    static
    {
        // Preheat HTTP request module so that subsequent access return similar delay
        try
        {
            Request.head("https://1.1.1.1").connectTimeout(ONE_S).execute();
        }
        catch (IOException ignored) {}
    }

    /**
     * Measure the ping (internet connectivity delay) to an url
     *
     * @param url Target url
     * @return Ping in milliseconds, or -1 if it cannot connect
     */
    public static int ping(String url)
    {
        var to = Timeout.ofSeconds(1);
        try
        {
            var start = System.nanoTime();

            // Do request and check code
            var head = Request.head(url).connectTimeout(to).responseTimeout(to)
                .execute().returnResponse();
            var status = head.getCode();
            if (status >= 400) return -1;

            // Return time
            return (int) ((System.nanoTime() - start) / 1e6);
        }
        catch (IOException e)
        {
            return -1;
        }
    }
}
