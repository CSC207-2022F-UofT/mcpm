package org.hydev.mcpm.client.database.inputs;

/**
 * Input for the ListAllPackagesBoundary
 * @param itemsPerPage The amount of items for each page.
 *                     If negative, pageNumber is ignored and all plugins are returned.
 * @param pageNumber The page number (starting at 0).
 * @param noCache If true, the ListPackagesBoundary will re-download the database before performing the request.
 */
public record ListPackagesInput(
    int itemsPerPage,
    int pageNumber,
    boolean noCache
) { }
