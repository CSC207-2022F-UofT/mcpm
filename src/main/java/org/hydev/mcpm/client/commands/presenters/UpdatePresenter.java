package org.hydev.mcpm.client.commands.presenters;

import org.hydev.mcpm.client.updater.UpdateInput;
import org.hydev.mcpm.client.updater.UpdateResult;

public interface UpdatePresenter {

    // Note from LogUpdatePresented: Taking input seems to allow for a nicer formatting.
    void present(UpdateInput input, UpdateResult result);
}
