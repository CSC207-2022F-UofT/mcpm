package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.export.ExportPluginsBoundary;
import org.hydev.mcpm.client.export.ExportPluginsInput;
import org.hydev.mcpm.client.export.ExportPluginsResult;

import java.util.ArrayList;
import java.util.List;

public class MockExportBoundary implements ExportPluginsBoundary {
    private final List<ExportPluginsInput> inputs = new ArrayList<>();

    private ExportPluginsResult.State defaultResult = ExportPluginsResult.State.SUCCESS;

    @Override
    public ExportPluginsResult export(ExportPluginsInput input) {
        inputs.add(input);

        return new ExportPluginsResult(defaultResult);
    }

    public void setDefaultResult(ExportPluginsResult.State defaultResult) {
        this.defaultResult = defaultResult;
    }

    public List<ExportPluginsInput> getInputs() {
        return List.copyOf(inputs);
    }
}
