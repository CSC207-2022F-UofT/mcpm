package org.hydev.mcpm.client.commands.entries;

import org.apache.commons.lang3.NotImplementedException;
import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.injector.LoadBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;

import java.util.List;
import java.util.function.Consumer;

/**
 * Handles the user input for a search.
 */

public class SearchPackagesController {
    private final SearchPackagesBoundary searcher;

    /**
     * Creates a LoadCommand object with this specified LoadBoundary to use when dispatched.
     *
     * @param searcher The load boundary to use in Command operation.
     */
    public SearchPackagesController(SearchPackagesBoundary searcher) {
        this.searcher = searcher;
    }


    /**
     * Load plugins and output status to log.
     *
     * @param pluginNames A list of all plugin names to be loaded.
     * @param log Callback for status for log events.
     */
    public void searchPackages(List<String> pluginNames, Consumer<String> log) {
        throw new NotImplementedException();
    }
}