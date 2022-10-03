package org.hydev.mcpm.client.models;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.util.*;

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
    ArrayList<String> authors;
    String website;
    ArrayList<String> depend;
    String prefix;
    ArrayList<String> softdepend;
    ArrayList<String> loadbefore;
    ArrayList<String> libraries;
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
        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);

        yml = yml.replaceAll("api-version", "apiVersion");
        return new Yaml(new Constructor(PluginYml.class), representer).load(yml);
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

    public ArrayList<String> getAuthors()
    {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors)
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

    public ArrayList<String> getDepend()
    {
        return depend;
    }

    public void setDepend(ArrayList<String> depend)
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

    public ArrayList<String> getSoftdepend()
    {
        return softdepend;
    }

    public void setSoftdepend(ArrayList<String> softdepend)
    {
        this.softdepend = softdepend;
    }

    public ArrayList<String> getLoadbefore()
    {
        return loadbefore;
    }

    public void setLoadbefore(ArrayList<String> loadbefore)
    {
        this.loadbefore = loadbefore;
    }

    public ArrayList<String> getLibraries()
    {
        return libraries;
    }

    public void setLibraries(ArrayList<String> libraries)
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return this.hashCode() == o.hashCode();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(main, name, version, description, apiVersion, load, author, authors, website, depend, prefix, softdepend, loadbefore, libraries, commands, permissions);
    }

    @Override
    public String toString()
    {
        return "PluginYml{" +
            "main='" + main + '\'' +
            ", name='" + name + '\'' +
            ", version='" + version + '\'' +
            ", description='" + description + '\'' +
            ", apiVersion='" + apiVersion + '\'' +
            ", load='" + load + '\'' +
            ", author='" + author + '\'' +
            ", authors=" + authors +
            ", website='" + website + '\'' +
            ", depend=" + depend +
            ", prefix='" + prefix + '\'' +
            ", softdepend=" + softdepend +
            ", loadbefore=" + loadbefore +
            ", libraries=" + libraries +
            ", commands=" + commands +
            ", permissions=" + permissions +
            '}';
    }
}
