package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.export.ImportResult;
import org.hydev.mcpm.client.interaction.ILogger;

/**
 * Interface for displaying the results of an import
 */
public interface ImportPresenter {

    /**
     * Present the result of the import
     *
     * @param result the result to present
     */
    void present(ImportResult result, ILogger log);
}
