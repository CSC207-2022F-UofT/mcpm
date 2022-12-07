package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.display.presenters.Table;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.search.SearchPackagesBoundary;
import org.hydev.mcpm.client.search.SearchPackagesInput;
import org.hydev.mcpm.client.search.SearchPackagesResult;
import org.hydev.mcpm.client.search.SearchPackagesType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Handles the user input for a search.
 *
 */
public record SearchPackagesController(SearchPackagesBoundary searcher) {

    /**
     * Load plugins and output status to log.
     *
     * @param type String that specifies the type of search.
     * @param text String that specifies the search text.
     * @param noCache Specifies whether to use local cache or not.
     *
     * @return The search result.
     */
    public SearchPackagesResult searchPackages(String type, String text, boolean noCache) {
        SearchPackagesInput inp = new SearchPackagesInput(
                SearchPackagesType.valueOf("BY_" + type.toUpperCase()),
                text,
                noCache);
        return searcher.search(inp);
    }
}
