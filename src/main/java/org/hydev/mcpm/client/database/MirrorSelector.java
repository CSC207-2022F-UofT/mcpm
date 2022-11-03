package org.hydev.mcpm.client.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.hc.client5.http.fluent.Request;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Implementation of mirror select boundary
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-01
 */
public class MirrorSelector implements MirrorSelectBoundary
{
    public static final String MIRROR_LIST_URL = "https://mcprs.hydev.org/mirrorlist.yml";
    private static final ObjectMapper YML = new ObjectMapper(new YAMLFactory());
    private static final File LOCAL_PATH = new File(System.getProperty("user.home"), ".config/mcpm/mirrorlist.yml");

    @Override
    public @NotNull List<Mirror> listAvailableMirrors() throws IOException
    {
        // Check if file exists
        if (!LOCAL_PATH.isFile()) updateMirrors();

        // Read
        var yml = Files.readString(LOCAL_PATH.toPath());
        return YML.readValue(yml, new TypeReference<>() {});
    }

    @Override
    public void updateMirrors() throws IOException
    {
        // Send request to fetch mirror list
        var yml = Request.get(MIRROR_LIST_URL).execute().returnContent().asString();

        // Try parsing the YML
        YML.readValue(yml, new TypeReference<List<Mirror>>(){});

        // Save yml to local folder
        LOCAL_PATH.getParentFile().mkdirs();
        Files.writeString(LOCAL_PATH.toPath(), yml);
    }
}
