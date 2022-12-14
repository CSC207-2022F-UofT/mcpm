package org.hydev.mcpm.client.display.presenters;

import org.hydev.mcpm.client.commands.controllers.PageBoundary;
import org.hydev.mcpm.client.commands.presenters.SearchResultPresenter;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.search.SearchPackagesResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Implementation to the SearchResultPresenter interface, displaying the result of the search command.
 */
public class SearchPresenter implements SearchResultPresenter {
    @Nullable
    private final PageBoundary pageController;

    /**
     * Instantiate Install Presenter
     *
     * @param pageController controller for pagination.
     */

    public SearchPresenter(@org.jetbrains.annotations.Nullable PageBoundary pageController) {
        this.pageController = pageController;
    }

    @Override
    public void displayResult(SearchPackagesResult result, Consumer<String> log) {
        log.accept("Search State: " + result.state().toString());

        // Print the plugins found
        var list = result.plugins().stream()
                .map(PluginModel::getLatestPluginVersion)
                .filter(Optional::isPresent)
                .map(it -> it.get().meta())
                .toList();

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
