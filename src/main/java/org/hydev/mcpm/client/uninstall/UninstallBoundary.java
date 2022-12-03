package org.hydev.mcpm.client.uninstall;

/**
 * Uninstalls a plugin
 */
public interface UninstallBoundary {
    /**
     * Uninstalls plugin based on its given name
     *
     * @param input Uninstall input
     */
    UninstallResult uninstall(UninstallInput input);
}
