package org.hydev.mcpm.client.database.inputs;

/**
 * Input for the SearchPackagesBoundary.
 *
 * @param type Specifies the type of search.
 * @param searchStr String containing the name, keyword, or command to search by.
 * @param noCache If true, the ListPackagesBoundary will re-download the database before performing the request.
 *
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 */
public record SearchPackagesInput(Type type, String searchStr, boolean noCache) {
    /**
     * The possible types of searching.
     * Currently consists of BY_NAME, BY_COMMAND, BY_KEYWORD.
     */
    public enum Type {
        BY_NAME,
        BY_COMMAND,
        BY_KEYWORD
    }
}
