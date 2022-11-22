package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.inputs.SearchPackagesType;

import java.util.List;
import java.util.function.Consumer;

import static org.hydev.mcpm.utils.FormatUtils.tabulate;

/**
 * Handles the user input for a search.
 */

public class SearchPackagesController {
    private final SearchPackagesBoundary searcher;

    /**
     * Creates a searcher object with this specified SearchPackagesBoundary to use when dispatched.
     *
     * @param searcher The search boundary to use in Search operation.
     */
    public SearchPackagesController(SearchPackagesBoundary searcher) {
        this.searcher = searcher;
    }


    /**
     * Load plugins and output status to log.
     *
     * @param type String that specifies the type of search.
     * @param text String that secifies the search text.
     * @param noCache Specifies whether to use local cache or not.
     * @param log Callback for status for log events.
     */
    public void searchPackages(String type, String text, boolean noCache, Consumer<String> log) {
        SearchPackagesInput inp = new SearchPackagesInput(
                SearchPackagesType.valueOf("BY_" + type.toUpperCase()),
                text,
                noCache);
        var result = searcher.search(inp);
        log.accept("Search State: " + result.state().toString());

        // Print the plugins found
        var list = result.plugins().stream()
                .map(it -> it.getLatestPluginVersion().get().meta()).toList();
        var table = tabulate(list.stream().map(p -> List.of(
                "&a" + p.name(), "&e" + p.getFirstAuthor(), p.version())).toList(),
                List.of(":Name", "Author", "Version:"));
        log.accept(table);
    }
}
