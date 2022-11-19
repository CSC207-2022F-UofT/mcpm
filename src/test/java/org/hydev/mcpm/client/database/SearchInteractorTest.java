package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.arguments.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.database.searchusecase.SearchInteractor;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Test suite for the SearchInteractor class.
 *
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 */
public class SearchInteractorTest {
    private URI host = URI.create("http://mcpm.hydev.org");
    private LocalDatabaseFetcher fetcher = new LocalDatabaseFetcher(host);
    private SearchInteractor database = new SearchInteractor(fetcher);

    @Test
    void testSearchByNameSUCCESS() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesInput.Type.BY_NAME, "SkinsRestorer", true));

        assert result.state() == SearchPackagesResult.State.SUCCESS;

        var text = result
                .plugins()
                .stream()
                .map(x -> x.versions().stream().findFirst().map(value -> value.meta().name()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(value -> "  " + value)
                .collect(Collectors.joining("\n"));

        System.out.println(text);
        // assert text == ;
    }
}
