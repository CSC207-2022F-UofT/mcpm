package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.commands.presenters.SearchResultPresenter;
import org.hydev.mcpm.client.interaction.ILogger;
import org.hydev.mcpm.client.search.SearchPackagesResult;

/**
 * This class is a mock search presenter that logs all inputs to a string.
 * It is used for tests involving a search presenter.
 */
public class MockSearchPresenter implements SearchResultPresenter {
    @Override
    public void displayResult(SearchPackagesResult searchResult, ILogger log) {
        /* do nothing */
    }
}
