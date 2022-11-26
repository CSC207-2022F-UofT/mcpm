package org.hydev.mcpm.client.uninstall;

import org.hydev.mcpm.client.injector.PluginNotFoundException;

/**
 * Uninstalls a plugin
 */
public interface UninstallBoundary {
    /**
     * Uninstalls plugin based on its given name
     *
     * @param name given name of plugin
     */
    void uninstall(String name) throws PluginNotFoundException;
}
