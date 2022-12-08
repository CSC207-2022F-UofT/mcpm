package org.hydev.mcpm.client.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.hydev.mcpm.client.export.storage.StringStorage;
import org.hydev.mcpm.client.export.storage.StringStorageFactory;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.search.SearchPackagesType;
import org.hydev.mcpm.utils.Pair;

import java.io.IOException;
import java.util.List;

import static org.hydev.mcpm.Constants.JACKSON;

/**
 * Default implementation of ImportPluginsBoundary
 *
 * @param install the installer boundary
 */
public record ImportInteractor(InstallBoundary install) implements ImportPluginsBoundary {
    @Override
    public ImportResult importPlugins(ImportInput input) {
        try {
            StringStorage storage = StringStorageFactory.createStringStorage(input);
            String json = storage.load(input.input());
            var plugins = JACKSON.readValue(json, new TypeReference<List<ExportModel>>() { });
            // TODO: Install specific versions
            var results = plugins.stream()
                    .map(p -> new Pair<>(p.name(),
                            install.installPlugin(new InstallInput(p.name(), SearchPackagesType.BY_NAME, true, true),
                                    (installResult, name) -> {

                                    })))
                    .collect(Pair.toMap());

            return new ImportResult(results);
        } catch (JsonProcessingException e) {
            return new ImportResult("Import source is invalid or corrupted.");
        } catch (IOException e) {
            return new ImportResult("Unable to read from import source." +
                    " Check if you imported from the right source.");
        }
    }
}
