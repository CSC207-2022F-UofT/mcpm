package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.presenters.UpdatePresenter;
import org.hydev.mcpm.client.updater.UpdateBoundary;
import org.hydev.mcpm.client.updater.UpdateInput;

import java.util.List;

public record UpdateController(UpdateBoundary boundary) {
    public void update(List<String> names, boolean load, boolean noCache, UpdatePresenter presenter) {
        var input = new UpdateInput(names, load, noCache);
        var result = boundary.update(input);

        // This is being done in the Controller for this time being.
        // Rena suggested that we move this down into the boundary, I am okay either way.
        presenter.present(input, result);
    }
}
