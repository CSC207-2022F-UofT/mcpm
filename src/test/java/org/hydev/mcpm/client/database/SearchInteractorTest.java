package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.fetcher.BriefFetcherListener;
import org.hydev.mcpm.client.database.fetcher.ConstantFetcher;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.database.searchusecase.SearchInteractor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Test suite for the SearchInteractor class.
 *
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 */
public class SearchInteractorTest {
    private static SearchInteractor database;

    /**
     * Create relevant interactors for tests.
     */
    @BeforeAll
    public static void setup() {
        var smallFetcher = new ConstantFetcher(PluginMockFactory.generateTestPlugins());
        var listener = new BriefFetcherListener(true);
        database = new SearchInteractor(smallFetcher, listener);
    }

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
                new SearchPackagesInput(SearchPackagesType.BY_NAME, "multiverse-core", true));

        assert result.state() == SearchPackagesResult.State.SUCCESS;

        var text = formatStr(result, ", ");
        assert text.equals("Multiverse-Core");
    }

    @Test
    void testSearchByKeywordSuccess() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_KEYWORD, "players and", true));

        assert result.state() == SearchPackagesResult.State.SUCCESS;

        var text = formatStr(result, ", ");
        assert text.equals("Holographic Displays, WorldGuard");
    }

    @Test
    void testSearchByCommandSuccess() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_COMMAND, "/ungod", true));

        assert result.state() == SearchPackagesResult.State.SUCCESS;

        var text = formatStr(result, ", ");
        assert text.equals("WorldGuard, Holographic Displays");
    }
}
