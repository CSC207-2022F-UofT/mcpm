package org.hydev.mcpm.client.database.searchusecase;

import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;

/**
 * Factory that produces Searcher objects based on the input type.
 *
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 */
public class SearcherFactory {

    /**
     * Returns the new searcher object based on the input type.
     *
     * @param input Contains the search type in particular. See SearchPackagesInput for details.
     */
    public static Searcher createSearcher(SearchPackagesInput input) {
        return switch (input.type()) {
            case BY_NAME ->
                    new SearcherByName();

            case BY_COMMAND ->
                    new SearcherByCommand();

            case BY_KEYWORD ->
                    new SearcherByKeyword();

        };
    }
}
