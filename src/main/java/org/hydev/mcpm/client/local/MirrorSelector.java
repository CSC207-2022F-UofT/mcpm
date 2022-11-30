package org.hydev.mcpm.client.local;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.hc.client5.http.fluent.Request;
import org.hydev.mcpm.client.database.mirrors.Mirror;
import org.hydev.mcpm.client.database.mirrors.MirrorSelectBoundary;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.hydev.mcpm.Constants.CFG_PATH;

/**
 * Implementation of mirror select boundary
 */
public record MirrorSelector(String mirrorListUrl) implements MirrorSelectBoundary
{
    public MirrorSelector()
    {
        this("https://mcprs.hydev.org/mirrorlist.yml");
    }

    private static final ObjectMapper YML = new ObjectMapper(new YAMLFactory());
    private static final File LOCAL_PATH = new File(CFG_PATH, "mirrorlist.yml");
    private static final File LOCAL_SEL = new File(CFG_PATH, "selected.yml");

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
        var yml = Request.get(mirrorListUrl()).execute().returnContent().asString();

        // Try parsing the YML
        YML.readValue(yml, new TypeReference<List<Mirror>>(){});

        // Save yml to local folder
        LOCAL_PATH.getParentFile().mkdirs();
        Files.writeString(LOCAL_PATH.toPath(), yml);
    }

    @Override
    public Mirror getSelectedMirror() throws IOException
    {
        if (LOCAL_SEL.exists()) return YML.readValue(Files.readString(LOCAL_SEL.toPath()), new TypeReference<>() {});
        return listAvailableMirrors().get(0);
    }

    @Override
    public void setSelectedMirror(Mirror mirror) throws IOException
    {
        LOCAL_SEL.getParentFile().mkdirs();
        Files.writeString(LOCAL_SEL.toPath(), YML.writeValueAsString(mirror));
    }
}
