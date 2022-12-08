package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.export.ImportResult;

import java.util.function.Consumer;

/**
 * Interface for displaying the results of an import
 */
public interface ImportPresenter {

    /**
     * Present the result of the import
     *
     * @param result the result to present
     */
    void present(ImportResult result, Consumer<String> log);
}
