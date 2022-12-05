package org.hydev.mcpm.client.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.utils.Pair;

import java.util.List;

import static org.hydev.mcpm.Constants.JACKSON;

public record ImportInteractor(InstallBoundary install) implements ImportPluginsBoundary {
    @Override
    public ImportResult importPlugins(String input) throws ImportException {
        try {
            var plugins = JACKSON.readValue(input, new TypeReference<List<ExportModel>>() { });
            // TODO: Install specific versions
            var results = plugins.stream()
                    .map(p -> new Pair<>(p.name(),
                            install.installPlugin(new InstallInput(p.name(), SearchPackagesType.BY_NAME, true, true))))
                    .collect(Pair.toMap());

            return new ImportResult(ImportResult.State.SUCCESS, results);
        } catch (JsonProcessingException e) {
            throw new ImportException(e);
        }
    }
}
