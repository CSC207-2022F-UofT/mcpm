package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.models.Plugin;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loader for our custom database structure
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class DatabaseLoader
{
    /** Name index: map[lower-cased name] = Plugin of that name */
    private final Map<String, Plugin> nameIndex;

    /** Keyword index: map[lower keyword] = List[plugins that contain the keyword either in name or description] */
    private final Map<String, List<Plugin>> keywordIndex;

    /** Command index: map[lower command name/alias] = List[plugins that provide the command or alias] */
    private final Map<String, List<Plugin>> commandIndex;

    /**
     * Load database, create index for faster searching through the database
     *
     * @param path Database file path
     */
    public DatabaseLoader(String path)
    {
        this.nameIndex = new HashMap<>();
        this.keywordIndex = new HashMap<>();
        this.commandIndex = new HashMap<>();

        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Find the plugin by name
     *
     * @param name Name of the plugin
     * @return Plugin of that name, or null if not found
     */
    public Plugin findByName(String name)
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Search for a plugin by keyword.
     * <p>
     * This function should ignore letter case iin searching.
     * For example, searching "java" would match "Java" as well
     * <p>
     * This function implements fuzzy search that doesn't require the exact phrase to be available,
     * but requires all words in the phrase to be present.
     * For example, searching "java 11" would match "java jdk 11" but not "java"
     *
     * @param keyword Keyword
     * @return List of packages matching the keyword, or empty list
     */
    public List<Plugin> searchByKeyword(String keyword)
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Search for a plugin by command or command alias.
     *
     * @param command Command name
     * @return Plugins that provides the command, or empty list
     */
    public List<Plugin> searchByCommand(String command)
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }
}