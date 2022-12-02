package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.database.ListAllBoundary;
import org.hydev.mcpm.client.database.boundary.CheckForUpdatesBoundary;

import java.util.List;
import java.util.function.Consumer;

import static org.hydev.mcpm.utils.FormatUtils.tabulate;

/**
 * Controller class for the ListAll use case.
 *
 * @author Kevin Chen
 * @author Azalea
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
        var list = listAllBoundary.listAll(parameter, checkForUpdatesBoundary);

        // Tabulate result
        var table = tabulate(list.stream().map(p -> List.of("&a" + p.name(), "&e"
                + p.getFirstAuthor(), p.version())).toList(),
                List.of(":Name", "Author", "Version:"));

        log.accept(table);
    }
}
