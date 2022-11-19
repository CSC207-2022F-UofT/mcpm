package org.hydev.mcpm.client.database.boundary;

import org.hydev.mcpm.client.database.inputs.ExportPluginsInput;
import org.hydev.mcpm.client.database.results.ExportPluginsResult;

public interface ExportPluginsBoundary {
    ExportPluginsResult export(ExportPluginsInput input);
}
