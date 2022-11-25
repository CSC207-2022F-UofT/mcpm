package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.presenter.Table;
import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.inputs.SearchPackagesType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Handles the user input for a search.
 *
 * @author Jerry
 */
public record SearchPackagesController(SearchPackagesBoundary searcher, @Nullable PageBoundary pageController) {

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
        var table = new Table(List.of(":Name", "Author", "Version:"),
                list.stream().map(p -> List.of("&a" + p.name(), "&e" + p.getFirstAuthor(), p.version())).toList());

        // Pagination
        if (pageController != null) {
            pageController.store(table);
            log.accept(pageController.formatPage(1));
        }
        else {
            log.accept(table.toString());
        }
    }
}
