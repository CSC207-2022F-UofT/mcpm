package org.hydev.mcpm.client.database.searchusecase;

import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;

public class SearcherFactory {

    public Searcher createSearcher(SearchPackagesInput input) {
        return switch(input.type()) {
            case BY_NAME ->
                    new SearcherByName();

            case BY_COMMAND ->
                    new SearcherByCommand();

            case BY_KEYWORD ->
                    new SearcherByKeyword();

        };
    }
}
