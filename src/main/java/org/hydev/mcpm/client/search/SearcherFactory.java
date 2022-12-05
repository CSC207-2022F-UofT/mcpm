package org.hydev.mcpm.client.search;

/**
 * Factory that produces Searcher objects based on the input type.
 *
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
