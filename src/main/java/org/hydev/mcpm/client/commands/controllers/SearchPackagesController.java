package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.search.SearchPackagesBoundary;
import org.hydev.mcpm.client.search.SearchPackagesInput;
import org.hydev.mcpm.client.search.SearchPackagesResult;
import org.hydev.mcpm.client.search.SearchPackagesType;

import java.util.List;

/**
 * Handles the user input for a search.
 *
 */
public record SearchPackagesController(SearchPackagesBoundary searcher) {
    /**
     * Load plugins and output status to log.
     *
     * @param type String that specifies the type of search.
     * @param keywords Strings that specifies the search text.
     * @param noCache Specifies whether to use local cache or not.
     *
     * @return The search result.
     */
    public SearchPackagesResult searchPackages(String type, List<String> keywords, boolean noCache) {
        SearchPackagesInput inp = new SearchPackagesInput(
                SearchPackagesType.valueOf("BY_" + type.toUpperCase()),
                String.join(" ", keywords),
                noCache);

        return searcher.search(inp);
    }
}
