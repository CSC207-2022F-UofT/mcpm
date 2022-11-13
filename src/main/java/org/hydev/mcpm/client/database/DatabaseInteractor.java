package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.models.PluginModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loader for our custom database structure
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class DatabaseInteractor implements SearchPluginBoundary
{
    /** Name index: map[lower-cased name] = Plugin of that name */
    private final Map<String, PluginModel> nameIndex;

    /** Keyword index: map[lower keyword] = List[plugins that contain the keyword either in name or description] */
    private final Map<String, List<PluginModel>> keywordIndex;

    /** Command index: map[lower command name/alias] = List[plugins that provide the command or alias] */
    private final Map<String, List<PluginModel>> commandIndex;

    /**
     * Load database, create index for faster searching through the database
     *
     * @param path Database file path
     */
    public DatabaseInteractor(String path)
    {
        this.nameIndex = new HashMap<>();
        this.keywordIndex = new HashMap<>();
        this.commandIndex = new HashMap<>();

        // TODO: Implement this


        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public PluginModel findByName(String name)
    {
        if (nameIndex.containsKey(name)) {
            return nameIndex.get(name);
        }
        return null;
    }

    @Override
    public List<PluginModel> searchByKeyword(String keyword)
    {
        if (keywordIndex.containsKey(keyword)) {
            return keywordIndex.get(keyword);
        }
        return List.of();
    }

    @Override
    public List<PluginModel> searchByCommand(String command)
    {
        if (commandIndex.containsKey(command)) {
            return commandIndex.get(command);
        }
        return List.of();
    }
}
