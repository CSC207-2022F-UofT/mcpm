package org.hydev.mcpm.client.models;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * POJO model for plugin.yml inside each Minecraft Bukkit/Spigot plugin.
 * <p>
 * For specifications of each field; please visit https://www.spigotmc.org/wiki/plugin-yml/
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-10-02
 */
public class PluginYml
{
    @NotNull String main;
    @NotNull String name;
    @NotNull String version;
    String description;
    String apiVersion;
    String load;
    String author;
    List<String> authors;
    String website;
    List<String> depend;
    String prefix;
    List<String> softdepend;
    List<String> loadbefore;
    List<String> libraries;
    Map<String, Object> commands;
    Map<String, Object> permissions;

    /**
     * Required-arguments constructor
     *
     * @param main Main class of the plugin
     * @param name Name of the plugin
     * @param version Version of the plugin
     */
    public PluginYml(@NotNull String main, @NotNull String name, @NotNull String version)
    {
        this.main = main;
        this.name = name;
        this.version = version;
    }

    /**
     * YAML parsing requires an no-args constructor
     */
    public PluginYml()
    {
        this("", "", "");
    }

    /**
     * Parse plugin.yml from yml string
     *
     * @param yml YML string
     * @return PluginYml object
     */
    public static PluginYml fromYml(String yml)
    {
        yml = yml.replaceAll("api-version", "apiVersion");
        return new Yaml(new Constructor(PluginYml.class)).load(yml);
    }

    @NotNull
    public String getMain()
    {
        return main;
    }

    public void setMain(@NotNull String main)
    {
        this.main = main;
    }

    @NotNull
    public String getName()
    {
        return name;
    }

    public void setName(@NotNull String name)
    {
        this.name = name;
    }

    @NotNull
    public String getVersion()
    {
        return version;
    }

    public void setVersion(@NotNull String version)
    {
        this.version = version;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getApiVersion()
    {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion)
    {
        this.apiVersion = apiVersion;
    }

    public String getLoad()
    {
        return load;
    }

    public void setLoad(String load)
    {
        this.load = load;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public List<String> getAuthors()
    {
        return authors;
    }

    public void setAuthors(List<String> authors)
    {
        this.authors = authors;
    }

    public String getWebsite()
    {
        return website;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    public List<String> getDepend()
    {
        return depend;
    }

    public void setDepend(List<String> depend)
    {
        this.depend = depend;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public List<String> getSoftdepend()
    {
        return softdepend;
    }

    public void setSoftdepend(List<String> softdepend)
    {
        this.softdepend = softdepend;
    }

    public List<String> getLoadbefore()
    {
        return loadbefore;
    }

    public void setLoadbefore(List<String> loadbefore)
    {
        this.loadbefore = loadbefore;
    }

    public List<String> getLibraries()
    {
        return libraries;
    }

    public void setLibraries(List<String> libraries)
    {
        this.libraries = libraries;
    }

    public Map<String, Object> getCommands()
    {
        return commands;
    }

    public void setCommands(Map<String, Object> commands)
    {
        this.commands = commands;
    }

    public Map<String, Object> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(Map<String, Object> permissions)
    {
        this.permissions = permissions;
    }
}
