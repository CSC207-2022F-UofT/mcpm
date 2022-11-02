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
    private static final int PING_ITERS = 3;

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
        try
        {
            var start = System.currentTimeMillis();

            // Do request and check code
            for (int i = 0; i < PING_ITERS; i++)
            {
                var status = Request.head(url).connectTimeout(ONE_S).responseTimeout(ONE_S)
                    .execute().returnResponse().getCode();
                if (status >= 400) return -1;
            }

            // Return time
            return (int) (System.currentTimeMillis() - start) / PING_ITERS;
        }
        catch (IOException e)
        {
            return -1;
        }
    }
}
