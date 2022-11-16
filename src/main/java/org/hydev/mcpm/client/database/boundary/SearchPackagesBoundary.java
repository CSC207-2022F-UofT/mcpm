package org.hydev.mcpm.client.database.boundary;

import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;

/**
 * Interface for searching plugins.
 *
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 */
public interface SearchPackagesBoundary {

    /**
     * Searches for plugins based on the provided name, keyword, or command.
     * The input contains the type of search.
     *
     * @param input Record of inputs as provided in SearchPackagesInput. See it for more info.
     * @return Packages result. See the SearchPackagesResult record for more info.
     */
    SearchPackagesResult search(SearchPackagesInput input);
}
