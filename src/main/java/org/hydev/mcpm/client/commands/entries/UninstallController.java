package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.injector.PluginNotFoundException;
import org.hydev.mcpm.client.uninstall.UninstallBoundary;
import org.hydev.mcpm.client.uninstall.UninstallInput;
import org.hydev.mcpm.client.uninstall.UninstallResult;

/**
 * Controller for the uninstallation use case
 *
 * @author Anushka (https://github.com/aanushkasharma)
 * @since 2022-11-27
 */
public record UninstallController(UninstallBoundary boundary) {
    /**
     * Uninstall a plugin
     *
     * @param name Name of the plugin
     * @param recursive Whether to remove orphan dependencies
     * @return Uninstall result
     * @throws PluginNotFoundException Plugin not found
     */
    public UninstallResult uninstall(String name, boolean recursive) throws PluginNotFoundException {
        return boundary.uninstall(new UninstallInput(name, recursive));
    }
}
