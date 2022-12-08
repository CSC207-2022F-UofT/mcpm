package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.commands.presenters.UpdatePresenter;
import org.hydev.mcpm.client.updater.UpdateBoundary;
import org.hydev.mcpm.client.updater.UpdateInput;

import java.util.List;

/**
 * Controller for the update command.
 *
 * @param boundary Update requests are dispatched to this boundary.
 */
public record UpdateController(UpdateBoundary boundary) {
    /**
     * Invoke this to dispatch an update request to the boundary.
     *
     * @param names A list of names to update.
     * @param load Whether to reload installed plugins (ignored on CLI environment).
     * @param noCache Whether to force fetch the database before updating.
     */
    public void update(List<String> names, boolean load, boolean noCache, UpdatePresenter presenter) {
        var input = new UpdateInput(names, load, noCache);
        var result = boundary.update(input);

        // This is being done in the Controller for this time being.
        // Rena suggested that we move this down into the boundary, I am okay either way.
        presenter.present(input, result);
    }
}
