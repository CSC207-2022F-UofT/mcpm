package org.hydev.mcpm.client.installer;


import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.installer.presenter.ResultPresenter;

/**
 * Interface for installing plugin to the jar file.
 */

public interface InstallBoundary {

    /**
     * Install a plugin
     *
     * @param installInput Options
     * @param resultPresenter resultPresenter
     */
    boolean installPlugin(InstallInput installInput, ResultPresenter resultPresenter);
}
