package org.hydev.mcpm.client.database.export;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;

import java.io.IOException;

/**
 * Store string content to a pastebin that supports raw text requests (e.g. https://github.com/w4/bin)
 *
 * @param host Pastebin host URL
 * @author Peter (https://github.com/MstrPikachu)
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-23
 */
public record PasteBinStorage(String host) implements StringStorage
{
    /**
     * Construct with default host
     */
    public PasteBinStorage()
    {
        this("https://mcpm.hydev.org/paste/");
    }

    @Override
    public String store(String content) throws IOException
    {
        // Send request
        return Request.put(host)
            .bodyString(content, ContentType.TEXT_PLAIN)
            .execute().returnContent().asString();
    }

    @Override
    public String load(String token) throws IOException
    {
        // If token is not a URL, treat it as the pastebin token and append host from it
        if (!token.startsWith("http://") && !token.startsWith("https://"))
        {
            token = host + token;
        }

        // Send request
        return Request.get(token)
            .addHeader("content-type", "text/plain")
            .execute().returnContent().asString();
    }

    @Override
    public String instruction()
    {
        return String.format("Please save the content to the pastebin at %s, and paste in the URL", host);
    }
}
