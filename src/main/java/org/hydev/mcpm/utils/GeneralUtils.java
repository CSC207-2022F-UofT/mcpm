package org.hydev.mcpm.utils;

import org.apache.hc.core5.net.URIBuilder;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class GeneralUtils
{
    /**
     * Make an url with parameters
     *
     * @param url Base url
     * @param parameters Parameters in pairs (length must be multiple of two)
     * @return URI object
     */
    public static URI makeUrl(String url, Object... parameters)
    {
        try
        {
            var builder = new URIBuilder(url);

            for (int i = 0; i < parameters.length; i += 2)
            {
                builder.addParameter(parameters[i].toString(), parameters[i + 1].toString());
            }

            return builder.build();
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sleep but throw runtime exception instead of regular exception
     *
     * @param ms Duration to sleep in ms
     */
    public static void safeSleep(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get resource file stored in this jar
     *
     * @param path Local file path
     * @return Absolute file path
     */
    public static File getResourceFile(String path)
    {
        URL url = GeneralUtils.class.getClassLoader().getResource(path);
        if (url == null) return null;

        try
        {
            return new File(url.toURI());
        }
        catch (URISyntaxException e)
        {
            return new File(url.getPath());
        }
    }
}
