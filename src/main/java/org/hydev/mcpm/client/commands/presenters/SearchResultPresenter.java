package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.search.SearchPackagesResult;

import java.util.function.Consumer;

/**
 * Interface for presenting the search result.
 *
 */
public interface SearchResultPresenter {
    /**
     * Display the string to the console.
     *
     * @param searchResult results of the search.
     * @param log log Callback for status for log events.
     */
    void displayResult(SearchPackagesResult searchResult, Consumer<String> log);
}
