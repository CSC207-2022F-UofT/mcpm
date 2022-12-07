package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.list.ListAllBoundary;
import org.hydev.mcpm.client.updater.CheckForUpdatesBoundary;
import org.hydev.mcpm.client.list.ListType;

import java.util.List;
import java.util.function.Consumer;

import static org.hydev.mcpm.client.display.presenters.Table.tabulate;

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
     * @param log       Logger
     */
    public void listAll(String parameter, Consumer<String> log) {

        ListType listType;
        switch (parameter.toLowerCase()) {
            case "all":
                listType = ListType.ALL;
                break;
            case "manual":
                listType = ListType.MANUAL;
                break;
            case "automatic":
                listType = ListType.AUTOMATIC;
                break;
            case "outdated":
                listType = ListType.OUTDATED;
                break;
            default:
                log.accept("Invalid parameter. Please use 'all', 'manual', 'automatic', or 'outdated'.");
                return;
        }

        var list = listAllBoundary.listAll(listType);

        // Tabulate result
        var table = tabulate(
                list.stream().map(p -> List.of("&a" + p.name(), "&e" + p.getFirstAuthor(), p.version())).toList(),
                List.of(":Name", "Author", "Version:"));

        log.accept(table);
    }
}
