package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.updater.UpdateInput;
import org.hydev.mcpm.client.updater.UpdateResult;

/**
 * Interface for presenters to implement who can format update result objects.
 */
public interface UpdatePresenter {
    /**
     * Formats and displays an UpdateResult object.
     * This also takes an input object for a more detailed formatting.
     *
     * @param input The input object that dispatched the update request.
     * @param result The returned result object that contains update outcomes.
     */
    void present(UpdateInput input, UpdateResult result);
}
