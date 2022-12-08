package org.hydev.mcpm.client.arguments.mock;

import org.hydev.mcpm.client.export.ExportPluginsBoundary;
import org.hydev.mcpm.client.export.ExportPluginsInput;
import org.hydev.mcpm.client.export.ExportPluginsResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a mock implementation of the ExportPluginsBoundary interface for testing.
 */
public class MockExportBoundary implements ExportPluginsBoundary {
    private final List<ExportPluginsInput> inputs = new ArrayList<>();

    private ExportPluginsResult.State defaultResult = ExportPluginsResult.State.SUCCESS;

    @Override
    public ExportPluginsResult export(ExportPluginsInput input) {
        inputs.add(input);

        return new ExportPluginsResult(defaultResult, "", null);
    }

    /**
     * Sets the default result that this interface will return when invoked.
     *
     * @param defaultResult The default return value for the export method.
     */
    public void setDefaultResult(ExportPluginsResult.State defaultResult) {
        this.defaultResult = defaultResult;
    }

    /**
     * Gets a list of all inputs that this interface was invoked with.
     *
     * @return A list of input objects.
     */
    public List<ExportPluginsInput> getInputs() {
        return List.copyOf(inputs);
    }
}
