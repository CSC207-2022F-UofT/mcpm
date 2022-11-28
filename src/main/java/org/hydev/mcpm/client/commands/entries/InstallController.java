package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.InstallResult;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.installer.presenter.InstallPresenter;
import org.hydev.mcpm.client.installer.presenter.ResultPresenter;

import java.util.function.Consumer;

/**
 * Controller class for the Install use case.
 */
public record InstallController(InstallBoundary boundary)
{
    /**
     * Install the plugin
     *
     * @param name Plugin name from repository
     * @param type The type of searching package
     * @param load Whether to load after installing
     * @param resultPresenter display the state of the plugin
     */
    public void install(String name, SearchPackagesType type, boolean load, ResultPresenter resultPresenter) {
        // TODO: User-friendly outputs
        var input = new InstallInput(name, type, load, true);
        var output = boundary.installPlugin(input, resultPresenter);
        // log.accept(output.type().reason());
    }
}
