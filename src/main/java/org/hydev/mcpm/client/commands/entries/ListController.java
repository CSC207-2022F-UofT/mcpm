package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.database.ListAllBoundary;

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

    /**
     * Constructor for ListAllController.
     *
     * @param listAllBoundary The boundary class for ListAllController.
     */
    public ListController(ListAllBoundary listAllBoundary) {
        this.listAllBoundary = listAllBoundary;
    }

    /**
     * Executes the ListAll use case.
     *
     * @param parameter The parameter for the ListAll use case.
     * @param log       Logger
     */
    public void listAll(String parameter, Consumer<String> log) {
        var list = listAllBoundary.listAll(parameter);

        // Tabulate result
        var table = tabulate(list.stream().map(p -> List.of("&a" + p.name(), "&e"
                + p.getFirstAuthor(), p.version())).toList(),
                List.of(":Name", "Author", "Version:"));

        log.accept(table);
    }
}
