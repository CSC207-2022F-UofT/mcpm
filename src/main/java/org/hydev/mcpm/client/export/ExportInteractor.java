package org.hydev.mcpm.client.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hydev.mcpm.client.database.tracker.PluginTracker;
import org.hydev.mcpm.client.export.storage.StringStorage;
import org.hydev.mcpm.client.export.storage.StringStorageFactory;

import java.io.IOException;

import static org.hydev.mcpm.Constants.JACKSON;

/**
 * An implementation of ExportPluginsBoundary that fetches from a database.
 */
public record ExportInteractor(PluginTracker tracker) implements ExportPluginsBoundary {

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
            return new ExportPluginsResult(ExportPluginsResult.State.FAILED, null,
                    "Could not fetch plugins");
        }
        var models = plugins.stream().map(p -> new ExportModel(p.name(), p.version())).toList();

        try {
            var answer = JACKSON.writeValueAsString(models);
            StringStorage storage = StringStorageFactory.createStringStorage(input);
            var token = storage.store(answer);
            return new ExportPluginsResult(ExportPluginsResult.State.SUCCESS, token, null);
        } catch (JsonProcessingException e) {
            // Should never happen
            throw new RuntimeException(e);
        } catch (IOException e) {
            return new ExportPluginsResult(ExportPluginsResult.State.FAILED, null, e.getMessage());
        }
    }
}
