package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.commands.presenters.UpdatePresenter;
import org.hydev.mcpm.client.updater.UpdateInput;
import org.hydev.mcpm.client.updater.UpdateResult;

/**
 * Update presenter implementation that does nothing when invoked.
 * Primarily used for testing, but can be used by other classes to discard presentation information.
 */
public class SilentUpdatePresenter implements UpdatePresenter {
    @Override
    public void present(UpdateInput input, UpdateResult result) {
        /* do nothing */
    }
}
