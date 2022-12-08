package org.hydev.mcpm.client.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.hydev.mcpm.client.export.storage.StringStorage;
import org.hydev.mcpm.client.export.storage.StringStorageFactory;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.input.FuzzyInstallInput;
import org.hydev.mcpm.client.installer.output.InstallResult;
import org.hydev.mcpm.client.search.SearchPackagesType;

import java.io.IOException;
import java.util.ArrayList;
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
            var results = new ArrayList<InstallResult>();
            for (var p : plugins) {
                results.addAll(install.installPlugin(
                        new FuzzyInstallInput(p.name(), SearchPackagesType.BY_NAME, true, true)));
            }

            return new ImportResult(results);
        } catch (JsonProcessingException e) {
            return new ImportResult("Import source is invalid or corrupted.");
        } catch (IOException e) {
            return new ImportResult("Unable to read from import source. " +
                    e.getMessage());
        }
    }
}
