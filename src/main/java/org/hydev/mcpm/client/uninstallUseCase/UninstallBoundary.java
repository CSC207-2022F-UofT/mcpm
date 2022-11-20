package org.hydev.mcpm.client.uninstallUseCase;

import org.hydev.mcpm.client.injector.PluginNotFoundException;

/**
 * Uninstalls a plugin
 */
public interface UninstallBoundary {
    /**
     * Uninstalls plugin based on given name
     * @param name given name of plugin
     */

    public void uninstall(String name) throws PluginNotFoundException;
}
