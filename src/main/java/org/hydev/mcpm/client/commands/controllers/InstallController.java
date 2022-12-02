package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.search.SearchPackagesType;
import org.hydev.mcpm.client.installer.InstallBoundary;
import org.hydev.mcpm.client.installer.InstallResult;
import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.commands.presenters.InstallResultPresenter;

import java.util.List;

/**
 * Controller class for the installation use case.
 */
public record InstallController(InstallBoundary boundary)
{
    /**
     * Install the plugin
     *
     * @param name Plugin name from repository
     * @param type The type of searching package
     * @param load Whether to load after installing
     * @return Results
     */
    public List<InstallResult> install(String name, SearchPackagesType type, boolean load) {
        var input = new InstallInput(name, type, load, true);
        return boundary.installPlugin(input);
    }
}
