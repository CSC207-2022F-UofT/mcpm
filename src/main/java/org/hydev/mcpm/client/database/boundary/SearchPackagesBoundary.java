package org.hydev.mcpm.client.database.boundary;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.models.PluginModel;

import java.util.List;

/**
 * Interface for searching plugins.
 *
 * @author Jerry Zhu (https://github.com/jerryzhu509)
 * @since 2022-10-29
 */
public interface SearchPackagesBoundary {

    /**
     * Searches for plugins based on the provided name, keyword, or command.
     * The input contains the type of search.
     *
     * @param input Record of inputs as provided in SearchPackagesInput. See it for more info.
     * @return Packages result. See the SearchPackagesResult record for more info.
     */
    public SearchPackagesResult search(SearchPackagesInput input);

//    /**
//     * Dynamically load a local plugin through JVM reflections and classloader hacks
//     *
//     * @param name Loaded plugin name
//     * @return A list of PluginModels with the given name.
//     */
//    public List<PluginModel> searchByName(List<PluginModel> plugins, String name);
//
//
//    /**
//     * Search for a plugin by keyword.
//     * <p>
//     * This function should ignore letter case in searching.
//     * For example, searching "java" would match "Java" as well
//     * <p>
//     * This function implements fuzzy search that doesn't require the exact phrase to be available,
//     * but requires all words in the phrase to be present.
//     * For example, searching "java 11" would match "java jdk 11" but not "java"
//     *
//     * @param keyword Keyword
//     * @return List of packages matching the keyword, or empty list
//     */
//    public List<PluginModel> searchByKeyword(List<PluginModel> plugins, String keyword);
//
//    /**
//     * Search for a plugin by command or command alias.
//     *
//     * @param command Command name
//     * @return Plugins that provides the command, or empty list
//     */
//    public List<PluginModel> searchByCommand(List<PluginModel> plugins, String command);
}
