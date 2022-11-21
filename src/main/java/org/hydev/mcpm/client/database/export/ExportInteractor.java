package org.hydev.mcpm.client.database.export;

import org.hydev.mcpm.client.database.LocalPluginTracker;
import org.hydev.mcpm.client.database.boundary.ExportPluginsBoundary;
import org.hydev.mcpm.client.database.inputs.ExportPluginsInput;
import org.hydev.mcpm.client.database.results.ExportPluginsResult;

import java.io.PrintStream;

/**
 * An implementation of ExportPluginsBoundary that fetches from a database.
 */
public class ExportInteractor implements ExportPluginsBoundary {

    private final LocalPluginTracker tracker;

    public ExportInteractor(LocalPluginTracker tracker) {
        this.tracker = tracker;
    }


    /**
     * Outputs the plugins on each line as its name and version separated by a space.
     *
     * @param input The output stream to write to
     * @return whether the export was successful or otherwise.
     */
    @Override
    public ExportPluginsResult export(ExportPluginsInput input) {
        var plugins = tracker.listInstalled();
        if (plugins == null) {
            return new ExportPluginsResult(ExportPluginsResult.State.FAILED_TO_FETCH_PLUGINS);
        }

        PrintStream ps = new PrintStream(input.out());
        plugins.forEach(p -> ps.printf("%s %s\n", p.name(), p.version()));
        return new ExportPluginsResult(ExportPluginsResult.State.SUCCESS);
    }
}
