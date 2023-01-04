package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.export.ExportPluginsResult;
import org.hydev.mcpm.client.interaction.ILogger;

/**
 * Interface for presenting the export use case
 */
public interface ExportPresenter {
    void present(ExportPluginsResult exportPluginsResult, ILogger log);
}
