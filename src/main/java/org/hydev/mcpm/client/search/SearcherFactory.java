package org.hydev.mcpm.client.search;

/**
 * Factory that produces Searcher objects based on the input type.
 *
 */
public class SearcherFactory {
    SearcherByName nameSearcher;
    SearcherByKeyword keywordSearcher;
    SearcherByCommand commandSearcher;

    /**
     * Returns the new searcher object based on the input type.
     *
     * @param input Contains the search type in particular. See SearchPackagesInput for details.
     */
    public Searcher createSearcher(SearchPackagesInput input) {
        return switch (input.type()) {
            case BY_NAME ->
                nameSearcher == null ?
                    nameSearcher = new SearcherByName() : nameSearcher;

            case BY_COMMAND ->
                commandSearcher == null ?
                        commandSearcher = new SearcherByCommand() : commandSearcher;

            case BY_KEYWORD ->
                keywordSearcher == null ?
                        keywordSearcher = new SearcherByKeyword() : keywordSearcher;
        };
    }
}
