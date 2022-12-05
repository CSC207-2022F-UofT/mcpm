package org.hydev.mcpm.client.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hydev.mcpm.client.database.tracker.PluginTracker;


import static org.hydev.mcpm.Constants.JACKSON;

/**
 * An implementation of ExportPluginsBoundary that fetches from a database.
 */
public record ExportInteractor(PluginTracker tracker, StringStorage storage) implements ExportPluginsBoundary {

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
            return new ExportPluginsResult(ExportPluginsResult.State.FAILED_TO_FETCH_PLUGINS, null);
        }
        var models = plugins.stream().map(p -> new ExportModel(p.name(), p.version())).toList();

        try {
            var answer = JACKSON.writeValueAsString(models);
            return new ExportPluginsResult(ExportPluginsResult.State.SUCCESS, answer);
        } catch (JsonProcessingException e) {
            // Should never happen
            throw new RuntimeException(e);
        }
    }
}
