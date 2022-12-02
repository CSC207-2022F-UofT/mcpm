package org.hydev.mcpm.utils;

import org.hydev.mcpm.client.models.PluginYml;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Static utility functions for reading Java archives (jar).
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-02
 */
public class PluginJarFile extends ZipFile
{
    public PluginJarFile(@NotNull File file) throws IOException
    {
        super(file);
    }

    /**
     * Read the content of a file inside a jar/zip file.
     *
     * @param innerPath Local path inside the jar/zip file (e.g. "./plugins.yml")
     * @return File content as byte array
     * @throws IOException Errors on reading the jar/zip file
     */
    public byte[] readBytes(String innerPath) throws IOException
    {
        return getInputStream(getEntry(innerPath)).readAllBytes();
    }

    /**
     * Read the content of a file inside a jar/zip file.
     *
     * @param innerPath Local path inside the jar/zip file (e.g. "./plugins.yml")
     * @return File content as string using UTF-8 encoding
     * @throws IOException Errors on reading the jar/zip file
     */
    public String readString(String innerPath) throws IOException
    {
        return new String(readBytes(innerPath), StandardCharsets.UTF_8);
    }

    /**
     * Read the content of the plugin.yml file inside a Minecraft plugin's jar
     *
     * @return Plugin.yml content
     * @throws IOException Errors on reading the jar/zip file
     */
    public PluginYml readPluginYaml() throws IOException, PluginYml.InvalidPluginMetaStructure
    {
        return PluginYml.fromYml(readString("plugin.yml"));
    }

    /**
     * List entries
     *
     * @return List of zip entries
     */
    public List<ZipEntry> list()
    {
        var entries = new ArrayList<ZipEntry>();
        var en = this.entries();
        while (en.hasMoreElements())
        {
            entries.add(en.nextElement());
        }
        return entries;
    }
}
