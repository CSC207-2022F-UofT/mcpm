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
    private final URI host = URI.create("http://mcpm.hydev.org");
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
                .map(value -> "  " + value)
                .collect(Collectors.joining(delim));
    }

    @Test
    void testSearchByNameSUCCESS() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesInput.Type.BY_NAME, "SkinsRestorer", true));

        assert result.state() == SearchPackagesResult.State.SUCCESS;

        var text = formatStr(result, ",");
        assert text.equals("SkinsRestorer,SkinsRestorer");
    }

    @Test
    void testSearchByKeywordSUCCESS() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesInput.Type.BY_COMMAND, "SkinsRestorer", true));

        assert result.state() == SearchPackagesResult.State.SUCCESS;

        var text = formatStr(result, ",");
        assert text.equals("CoordsOffline,StatusSigns,SkinsRestorer,PetShop,InventorySafe,SkinsRestorer");
    }

    @Test
    void testSearchByCommandSUCCESS() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesInput.Type.BY_COMMAND, "al", true));

        assert result.state() == SearchPackagesResult.State.SUCCESS;

        var text = formatStr(result, ",");

        System.out.println(text);
        assert text.equals("SkinsRestorer,SkinsRestorer");
    }
}
