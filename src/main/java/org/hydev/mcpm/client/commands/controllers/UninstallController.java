package org.hydev.mcpm.client.commands.controllers;

import org.hydev.mcpm.client.uninstall.UninstallBoundary;
import org.hydev.mcpm.client.uninstall.UninstallInput;
import org.hydev.mcpm.client.uninstall.UninstallResult;

/**
 * Controller for the uninstallation use case
 */
public record UninstallController(UninstallBoundary boundary) {
    /**
     * Uninstall a plugin
     *
     * @param name Name of the plugin
     * @param recursive Whether to remove orphan dependencies
     * @return Uninstall result
     */
    public UninstallResult uninstall(String name, boolean recursive) {
        return boundary.uninstall(new UninstallInput(name, recursive));
    }
}
