package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.export.ExportPluginsResult;

import java.util.function.Consumer;

/**
 * Interface for presenting the export use case
 */
public interface ExportPresenter {
    void present(ExportPluginsResult exportPluginsResult, Consumer<String> log);
}
