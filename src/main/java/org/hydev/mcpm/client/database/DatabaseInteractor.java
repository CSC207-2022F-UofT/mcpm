package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.database.boundary.ListPackagesBoundary;
import org.hydev.mcpm.client.database.boundary.SearchPackagesBoundary;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcherListener;
import org.hydev.mcpm.client.database.fetcher.LocalDatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.ProgressBarFetcherListener;
import org.hydev.mcpm.client.database.inputs.ListPackagesInput;
import org.hydev.mcpm.client.database.results.ListPackagesResult;
import org.hydev.mcpm.client.database.inputs.SearchPackagesInput;
import org.hydev.mcpm.client.database.results.SearchPackagesResult;
import org.hydev.mcpm.client.database.searchusecase.SearcherFactory;
import org.hydev.mcpm.client.models.PluginModel;
import org.hydev.mcpm.client.models.PluginVersion;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles fetching and performing operations on the plugin database.
 */
public class DatabaseInteractor implements ListPackagesBoundary, SearchPackagesBoundary {
    private final DatabaseFetcher fetcher;
    private final DatabaseFetcherListener listener;

    /**
     * Map associating each search type to the map associating a plugin feature to its
     * corresponding list of plugins.
     */
    private Map<SearchPackagesInput.Type, Map<String, List<PluginModel>>> searchMaps = new HashMap<>();

    /**
     * Creates a new database with the provided database fetcher.
     * Consider passing LocalDatabaseFetcher. Defaults to the ProgressBarFetcherListener.
     *
     * @param fetcher The fetcher that will be used to request the database object in boundary calls.
     */
    public DatabaseInteractor(DatabaseFetcher fetcher) {
        this(fetcher, new ProgressBarFetcherListener());
    }

    /**
     * Creates a new database with the provided fetcher and upload listener.
     *
     * @param fetcher The fetcher that will be used to request the database object in boundary calls.
     * @param listener The listener that will receive updates if the database is downloaded from the internet.
     */
    public DatabaseInteractor(DatabaseFetcher fetcher, DatabaseFetcherListener listener) {
        this.fetcher = fetcher;
        this.listener = listener;
        for (SearchPackagesInput.Type type: SearchPackagesInput.Type.values()) {
            this.searchMaps.put(type, null);
        }
    }

    /**
     * Lists all plugins in the database with pagination.
     *
     * @param input Provided input. See the ListPackagesInput record for more info.
     * @return Packages result. See the ListPackagesResult record for more info.
     */
    @Override
    public ListPackagesResult list(ListPackagesInput input) {
        var database = fetcher.fetchDatabase(!input.noCache(), listener);

        if (database == null) {
            return ListPackagesResult.by(ListPackagesResult.State.FAILED_TO_FETCH_DATABASE);
        }

        if (input.pageNumber() < 0) {
            return ListPackagesResult.by(ListPackagesResult.State.INVALID_INPUT);
        }

        var plugins = database.plugins();

        // Request all items.
        if (input.itemsPerPage() <= 0) {
            return new ListPackagesResult(
                ListPackagesResult.State.SUCCESS,
                input.pageNumber(),
                new ArrayList<>(plugins),
                database.plugins().size()
            );
        }

        var begin = input.itemsPerPage() * input.pageNumber();
        var end = Math.min(begin + input.itemsPerPage(), plugins.size());

        if (plugins.size() <= begin) {
            return ListPackagesResult.by(ListPackagesResult.State.NO_SUCH_PAGE);
        }

        var result = plugins.subList(begin, end);

        return new ListPackagesResult(
            ListPackagesResult.State.SUCCESS,
            input.pageNumber(),
            result,
            database.plugins().size()
        );
    }

    /**
     * Searches for plugins based on the provided name, keyword, or command.
     * The input contains the type of search.
     *
     * @param input Record of inputs as provided in SearchPackagesInput. See it for more info.
     * @return Packages result. See the SearchPackagesResult record for more info.
     */
    public SearchPackagesResult search(SearchPackagesInput input) {
        var database = fetcher.fetchDatabase(!input.noCache(), listener);

        if (database == null) {
            return SearchPackagesResult.by(SearchPackagesResult.State.FAILED_TO_FETCH_DATABASE);
        }

        var searchStr = input.searchStr();
        if (searchStr.isEmpty())
            return SearchPackagesResult.by(SearchPackagesResult.State.INVALID_INPUT);


        var plugins = database.plugins();

        // Make map if null
        SearcherFactory factory = new SearcherFactory();
        if (this.searchMaps.get(input.type()) == null)
            this.searchMaps.put(input.type(), factory.createSearcher(input).constructSearchMaps(plugins));

        return new SearchPackagesResult(SearchPackagesResult.State.SUCCESS,
                this.searchMaps.get(input.type()).get(searchStr));

//        return switch(input.type()) {
//            case BY_NAME ->
//                new SearchPackagesResult(
//                            SearchPackagesResult.State.SUCCESS,
//                            searchByName(plugins, searchStr));
//
//            case BY_COMMAND ->
//                new SearchPackagesResult(
//                        SearchPackagesResult.State.SUCCESS,
//                        searchByCommand(plugins, searchStr));
//
//            case BY_KEYWORD ->
//                new SearchPackagesResult(
//                        SearchPackagesResult.State.SUCCESS,
//                        searchByKeyword(plugins, searchStr));
//
//        };
    }

//    @Override
//    private static List<PluginModel> searchByName(List<PluginModel> plugins, String name)
//    {
////        List<PluginModel> models = plugins.stream()
////                .filter(p -> {
////                    var version = p.versions().stream().max(Comparator.comparingLong(PluginVersion::id)).orElse(null);
////                    if (version == null) return false;
////                    return version.meta().name().equals(name);
////                }).toList();
//
//        List<PluginModel> models = new ArrayList<>();
//        for (PluginModel plugin : plugins) {
//            // Get latest version
//            var v = plugin.versions().stream().max(Comparator.comparingLong(PluginVersion::id));
//            if (v.isPresent())
//                if (v.get().meta().name().equals(name))
//                    models.add(plugin);
//        }
//        return models;
//    }
//
//    @Override
//    private static List<PluginModel> searchByKeyword(List<PluginModel> plugins, String keyword)
//    {
//        List<PluginModel> models = new ArrayList<>();
//        for (PluginModel plugin : plugins) {
//            // Get latest version
//            var v = plugin.versions().stream().max(Comparator.comparingLong(PluginVersion::id));
//            if (v.isPresent())
//                if (v.get().meta().description().equals(keyword))
//                    models.add(plugin);
//        }
//        return models;
//    }
//
//    @Override
//    private static List<PluginModel> searchByCommand(List<PluginModel> plugins, String command)
//    {
//        // Todo
//        return List.of();
//    }

    /**
     * Demo main method for DatabaseInteractor.
     *
     * @param args Arguments are ignored.
     */
    public static void main(String[] args) {
        var host = URI.create("http://mcpm.hydev.org");
        var fetcher = new LocalDatabaseFetcher(host);
        var database = new DatabaseInteractor(fetcher);

        var result = database.list(new ListPackagesInput(20, 0, true));

        if (result.state() != ListPackagesResult.State.SUCCESS) {
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
