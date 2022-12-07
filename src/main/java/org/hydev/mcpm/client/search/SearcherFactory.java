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
        switch (input.type()) {
            case BY_NAME -> {
                if (nameSearcher == null)
                    nameSearcher = new SearcherByName();
                return nameSearcher;
            }
            case BY_COMMAND -> {
                if (commandSearcher == null)
                    commandSearcher = new SearcherByCommand();
                return commandSearcher;
            }
            case BY_KEYWORD -> {
                if (keywordSearcher == null)
                    keywordSearcher = new SearcherByKeyword();
                return keywordSearcher;
            }
        }
        return nameSearcher;
    }
}
