package org.hydev.mcpm.client.updater;

import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter;

import java.util.List;

/**
 * Input object to be passed to UpdateBoundary.
 *
 * @param pluginNames A list of all plugin names that should be updated.
 *                    An empty list will update all plugins.
 * @param load Whether to reload plugins that are installed.
 *             Ignored on a CLI environment.
 * @param noCache Whether to force fetch the database before checking updates.
 * @param installResultPresenter Presenter for installing state.
 */
public record UpdateInput(
    List<String> pluginNames,
    boolean load,
    boolean noCache,

    InstallResultPresenter installResultPresenter
) {
    /**
     * True if the input object should update all plugins (an empty pluginNames list).
     *
     * @return Whether to update all plugins.
     */
    public boolean updateAll() {
        return pluginNames.isEmpty();
    }
}
