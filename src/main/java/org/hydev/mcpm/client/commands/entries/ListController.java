package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.database.ListAllBoundary;

import java.util.List;

/**
 * Controller class for the ListAll use case.
 *
 * @author Kevin Chen
*/
public class ListController
{
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
     * @return The list of plugins.
     */
    public void listAll(String parameter) {
        System.out.println(listAllBoundary.listAll(parameter));
    }
}
