package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.commands.presenters.UpdatePresenter;
import org.hydev.mcpm.client.updater.UpdateInput;
import org.hydev.mcpm.client.updater.UpdateResult;

public class SilentUpdatePresenter implements UpdatePresenter {
    @Override
    public void present(UpdateInput input, UpdateResult result) {
        /* do nothing */
    }
}
