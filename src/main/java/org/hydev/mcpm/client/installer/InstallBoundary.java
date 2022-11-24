package org.hydev.mcpm.client.installer;


import org.hydev.mcpm.client.installer.input.InstallInput;

/**
 * Interface for installing plugin to the jar file.
 */

public interface InstallBoundary {

    /**
     * Install a plugin
     *
     * @param installInput Options
     */
    InstallResult installPlugin(InstallInput installInput);
}
