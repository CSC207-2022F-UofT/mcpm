package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.boundary.ListPackagesBoundary;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.ProgressBarFetcherListener;
import org.hydev.mcpm.client.database.inputs.ListPackagesInput;
import org.hydev.mcpm.client.database.inputs.ListPackagesResult;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class DatabaseInteractor implements ListPackagesBoundary {
    private final DatabaseFetcher fetcher;
    private final DatabaseFetcherListener listener;

    public DatabaseInteractor(DatabaseFetcher fetcher) {
        this(fetcher, new ProgressBarFetcherListener());
    }

    public DatabaseInteractor(DatabaseFetcher fetcher, DatabaseFetcherListener listener) {
        this.fetcher = fetcher;
        this.listener = listener;
    }

    @Override
    public ListPackagesResult list(ListPackagesInput input) {
        var database = fetcher.fetchDatabase(!input.noCache(), listener);

        if (database == null) {
            return ListPackagesResult.by(ListPackagesResult.State.FailedToFetchDatabase);
        }

        if (input.pageNumber() < 0) {
            return ListPackagesResult.by(ListPackagesResult.State.InvalidInput);
        }

        var plugins = database.plugins();

        // Request all items.
        if (input.itemsPerPage() <= 0) {
            return new ListPackagesResult(
                ListPackagesResult.State.Success,
                input.pageNumber(),
                new ArrayList<>(plugins),
                database.plugins().size()
            );
        }

        var begin = input.itemsPerPage() * input.pageNumber();
        var end = Math.min(begin + input.itemsPerPage(), plugins.size());

        if (plugins.size() <= begin) {
            return ListPackagesResult.by(ListPackagesResult.State.NoSuchPage);
        }

        var result = plugins.subList(begin, end);

        return new ListPackagesResult(
            ListPackagesResult.State.Success,
            input.pageNumber(),
            result,
            database.plugins().size()
        );
    }

    public static void main(String[] args) {
        var host = URI.create("http://mcpm.hydev.org");
        var fetcher = new LocalDatabaseFetcher(host);
        var database = new DatabaseInteractor(fetcher);

        var result = database.list(new ListPackagesInput(20, 0, true));

        if (result.state() != ListPackagesResult.State.Success) {
            System.out.println("Result Failed With State " + result.state().name());
            return;
        }

        System.out.println("Result (" + result.pageNumber() + " for " + result.plugins().size() + " plugins):");

        var text = result
            .plugins()
            .stream()
            .map(x -> x.versions().stream().findFirst().map(value -> value.meta().name()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(value -> "  " + value)
            .collect(Collectors.joining("\n"));

        System.out.println(text);
    }
}
