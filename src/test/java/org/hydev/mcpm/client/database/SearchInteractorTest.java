package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.fetcher.ConstantFetcher;
import org.hydev.mcpm.client.database.fetcher.QuietFetcherListener;
import org.hydev.mcpm.client.search.SearchPackagesInput;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.hydev.mcpm.client.search.SearchPackagesResult;
import org.hydev.mcpm.client.search.SearchInteractor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        var listener = new QuietFetcherListener();
        database = new SearchInteractor(smallFetcher, listener);
    }

    /**
     * Helper method for formatting the output as a string.
     *
     * @param result Search result.
     * @param delim Delimiter separating names.
     * @return Formatted names of plugins as a string.
     */
    @SuppressWarnings("SameParameterValue")
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
    void testSearchByNameSuccessMatch() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_NAME, "multiverse-core", true));

        assertEquals(result.state(), SearchPackagesResult.State.SUCCESS);

        var text = formatStr(result, ", ");
        assertEquals(text, "Multiverse-Core");
    }

    @Test
    void testSearchByNameSuccessNoMatch() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_NAME, "pp", true));

        assertEquals(result.state(), SearchPackagesResult.State.SUCCESS);

        var text = formatStr(result, ", ");
        assertEquals(text, "");
    }

    @Test
    void testSearchByKeywordSuccessMatch() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_KEYWORD, "players and", true));

        assertEquals(result.state(), SearchPackagesResult.State.SUCCESS);

        var text = formatStr(result, ", ");
        assertEquals(text, "Holographic Displays, WorldGuard");
    }

    @Test
    void testSearchByKeywordSuccessNoMatch() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_KEYWORD, "pp", true));

        assertEquals(result.state(), SearchPackagesResult.State.SUCCESS);

        var text = formatStr(result, ", ");
        assertEquals(text, "");
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    void testSearchByCommandSuccessMatch() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_COMMAND, "/ungod", true));

        assertEquals(result.state(), SearchPackagesResult.State.SUCCESS);

        var text = formatStr(result, ", ");
        assertEquals(text, "WorldGuard, Holographic Displays");
    }

    @Test
    void testSearchByCommandSuccessNoMatch() {
        var result = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_COMMAND, "pp", true));

        assertEquals(result.state(), SearchPackagesResult.State.SUCCESS);

        var text = formatStr(result, ", ");
        assertEquals(text, "");
    }

    @Test
    void testInvalidSearch() {
        var result1 = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_NAME, "", true));
        var result2 = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_KEYWORD, "", true));
        var result3 = database.search(
                new SearchPackagesInput(SearchPackagesType.BY_COMMAND, "", true));

        assertEquals(result1.state(), SearchPackagesResult.State.INVALID_INPUT);
        assertEquals(result2.state(), SearchPackagesResult.State.INVALID_INPUT);
        assertEquals(result3.state(), SearchPackagesResult.State.INVALID_INPUT);
        assert result1.plugins().isEmpty();
        assert result2.plugins().isEmpty();
        assert result3.plugins().isEmpty();
    }
}
