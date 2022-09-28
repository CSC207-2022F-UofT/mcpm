package org.hydev.mcpm;

import org.apache.hc.core5.net.URIBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class Utils
{
    /**
     * Make an url with parameters
     *
     * @param url Base url
     * @param parameters Parameters in pairs (length must be multiple of two)
     * @return URI object
     */
    public static URI makeUrl(String url, Object... parameters) throws URISyntaxException
    {
        var builder = new URIBuilder(url);

        for (int i = 0; i < parameters.length; i += 2)
        {
            builder.addParameter(parameters[i].toString(), parameters[i + 1].toString());
        }

        return builder.build();
    }
}
