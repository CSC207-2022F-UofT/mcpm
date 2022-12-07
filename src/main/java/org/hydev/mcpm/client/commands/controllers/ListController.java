package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.display.presenters.ListPresenter;
import org.hydev.mcpm.client.list.ListAllBoundary;

import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.list.ListType;
import org.hydev.mcpm.client.list.ListResult;



import java.util.function.Consumer;

/**
 * Controller class for the ListAll use case.
 *
 * @author Kevin Chen
 */
public class ListController {
    private final ListAllBoundary listAllBoundary;
    private final CheckForUpdatesBoundary checkForUpdatesBoundary;


    /**
     * Constructor for ListAllController.
     *
     * @param listAllBoundary The boundary class for ListAllController.
     */
    public ListController(ListAllBoundary listAllBoundary, CheckForUpdatesBoundary checkForUpdatesBoundary) {
        this.listAllBoundary = listAllBoundary;
        this.checkForUpdatesBoundary = checkForUpdatesBoundary;

    }

    /**
     * Executes the ListAll use case.
     *
     * @param parameter The parameter for the ListAll use case.
     */
    public void listAll(String parameter, Consumer<String> log) {
        ListPresenter listPresenter = new ListPresenter(log);
        ListType listType;
        switch (parameter.toLowerCase()) {
            case "all" -> listType = ListType.ALL;
            case "manual" -> listType = ListType.MANUAL;
            case "automatic" -> listType = ListType.AUTOMATIC;
            case "outdated" -> listType = ListType.OUTDATED;
            default -> {
                ListResult queryResult = new ListResult(null, ListResult.Type.SEARCH_INVALID_INPUT);
                listPresenter.displayResult(queryResult);
                return;
            }
        }

        try {
            var list = listAllBoundary.listAll(listType);

            // if list is empty
            ListResult queryResult;
            if (list.isEmpty()) {
                queryResult = new ListResult(list, ListResult.Type.SUCCESS_RETRIEVING_BUT_NO_MATCHES);
            } else {
                queryResult = new ListResult(list, ListResult.Type.SUCCESS_RETRIEVING_LOCAL_AND_UPDATABLE);
            }
            listPresenter.displayResult(queryResult);
        } catch (Exception e) {
            ListResult queryResult = new ListResult(null, ListResult.Type.SEARCH_FAILED_TO_FETCH_INSTALLED);
            listPresenter.displayResult(queryResult);
        }

    }
}
