package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.list.ListResult;

/**
 * Presenter for the list command
 */
public interface ListResultPresenter {
    /**
     * Display the associated output to the console
     *
     * @param listResult result of the list command
     */
    void displayResult(ListResult listResult);
}
