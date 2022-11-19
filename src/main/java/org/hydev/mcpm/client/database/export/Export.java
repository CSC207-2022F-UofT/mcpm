package org.hydev.mcpm.client.database.export;

import org.hydev.mcpm.client.database.boundary.ExportPluginsBoundary;
import org.hydev.mcpm.client.database.fetcher.DatabaseFetcher;
import org.hydev.mcpm.client.database.fetcher.ProgressBarFetcherListener;
import org.hydev.mcpm.client.database.inputs.ExportPluginsInput;
import org.hydev.mcpm.client.database.results.ExportPluginsResult;

import java.io.PrintStream;

public class Export implements ExportPluginsBoundary {

    private final DatabaseFetcher fetcher;

    public Export(DatabaseFetcher fetcher){
        this.fetcher = fetcher;
    }

    @Override
    public ExportPluginsResult export(ExportPluginsInput input) {
        var database = fetcher.fetchDatabase(input.cache(), new ProgressBarFetcherListener());
        if (database == null){
            return new ExportPluginsResult(ExportPluginsResult.State.FAILED_TO_FETCH_DATABASE);
        }
        var plugins = database.plugins();
        PrintStream ps = new PrintStream(input.out());
        plugins.forEach(p -> {
            var v = p.getLatestPluginVersion();
            v.ifPresent(pluginVersion ->
                    ps.printf("%s %s\n", pluginVersion.meta().name(), pluginVersion.meta().version()));
        });
        return new ExportPluginsResult(ExportPluginsResult.State.SUCCESS);
    }
}
