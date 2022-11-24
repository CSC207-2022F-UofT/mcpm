package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.database.searchusecase.SearchInteractor;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the SearchInteractor class.
 *
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 */
public class SearchInteractorTest {
    private final URI host = URI.create("http://mcprs-bell.hydev.org");
    private final LocalDatabaseFetcher fetcher = new LocalDatabaseFetcher(host);
    private final SearchInteractor database = new SearchInteractor(fetcher);

    /**
     * Helper method for formatting the output as a string.
     *
     * @param result Search result.
     * @param delim Delimiter separating names.
     * @return Formatted names of plugins as a string.
     */
    private String formatStr(SearchPackagesResult result, String delim) {
        return result
                .plugins()
                .stream()
                .map(x -> x.versions().stream().findFirst().map(value -> value.meta().name()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.joining(delim));
    }

    @Test
    void testSearchByNameSuccess() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_NAME, "SkinsRestorer", true));

        assert result.state() == SearchPackagesResult.State.SUCCESS;

        var text = formatStr(result, ", ");
        System.out.println(text);
        assertEquals(text, "SkinsRestorer, SkinsRestorer");
    }

    @Test
    void testSearchByKeywordSuccess() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_KEYWORD, "offline online", true));

        assert result.state() == SearchPackagesResult.State.SUCCESS;

        var text = formatStr(result, ", ");
        System.out.println(text);
        assertEquals(text, "CoordsOffline, StatusSigns, SkinsRestorer, PetShop, InventorySafe, SkinsRestorer");
    }

    @Test
    void testSearchByCommandSuccess() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_COMMAND, "al", true));

        assert result.state() == SearchPackagesResult.State.SUCCESS;

        var text = formatStr(result, ", ");

        System.out.println(text);
        assertEquals(text, "AnimatedLeaves");
    }
}
