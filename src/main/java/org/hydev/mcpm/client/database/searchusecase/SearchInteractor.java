package org.hydev.mcpm.client.database.searchusecase;

import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.arguments.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.arguments.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.arguments.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.arguments.fetcher.ProgressBarFetcherListener;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Handles searching within the database.
 *
 * @author Jerry Zhu (<a href="https://github.com/jerryzhu509">...</a>)
 */
public class SearchInteractor implements SearchPackagesBoundary {
    private final DatabaseFetcher fetcher;
    private final DatabaseFetcherListener listener;


    /**
     * Creates a new database with the provided database fetcher.
     * Consider passing LocalDatabaseFetcher. Defaults to the ProgressBarFetcherListener.
     *
     * @param fetcher The fetcher that will be used to request the database object in boundary calls.
     */
    public SearchInteractor(DatabaseFetcher fetcher) {
        this(fetcher, new ProgressBarFetcherListener());
    }

    /**
     * Creates a new database with the provided fetcher and upload listener.
     *
     * @param fetcher The fetcher that will be used to request the database object in boundary calls.
     * @param listener The listener that will receive updates if the database is downloaded from the internet.
     */
    public SearchInteractor(DatabaseFetcher fetcher, DatabaseFetcherListener listener) {
        this.fetcher = fetcher;
        this.listener = listener;

    }

    /**
     * Searches for plugins based on the provided name, keyword, or command.
     * The input contains the type of search.
     *
     * @param input Record of inputs as provided in SearchPackagesInput. See it for more info.
     * @return Packages result. See the SearchPackagesResult record for more info.
     */
    @Override
    public SearchPackagesResult search(SearchPackagesInput input) {
        var database = fetcher.fetchDatabase(!input.noCache(), listener);

        if (database == null) {
            return SearchPackagesResult.by(SearchPackagesResult.State.FAILED_TO_FETCH_DATABASE);
        }

        var searchStr = input.searchStr().toLowerCase();
        if (searchStr.isEmpty())
            return SearchPackagesResult.by(SearchPackagesResult.State.INVALID_INPUT);

        var plugins = database.plugins();

        return new SearchPackagesResult(SearchPackagesResult.State.SUCCESS,
                SearcherFactory.createSearcher(input).getSearchList(searchStr, plugins));
    }

    /**
     * Demo main method for DatabaseInteractor.
     *
     * @param args Arguments are ignored.
     */
    public static void main(String[] args) {
        var host = URI.create("http://mcpm.hydev.org");
        var fetcher = new LocalDatabaseFetcher(host);
        var database = new SearchInteractor(fetcher);

        var result = database.search(
                new SearchPackagesInput(SearchPackagesInput.Type.BY_NAME, "SkinsRestorer", true));

        if (result.state() != SearchPackagesResult.State.SUCCESS) {
            System.out.println("Result Failed With State " + result.state().name());
            return;
        }

        var text = result
                .plugins()
                .stream()
                .map(x -> x.versions().stream().findFirst().map(value -> value.meta().name()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(value -> "  " + value)
                .collect(Collectors.joining("\n"));

        System.out.println(text);
        System.out.println();
        
        var result3 = database.search(new SearchPackagesInput(
                SearchPackagesInput.Type.BY_KEYWORD, "offline online", true));
        var text3 = result3
                .plugins()
                .stream()
                .map(x -> x.versions().stream().findFirst().map(value -> value.meta().name()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(value -> "  " + value)
                .collect(Collectors.joining("\n"));
        System.out.println(text3);
    }
}
