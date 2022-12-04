package org.hydev.mcpm.client.installer;


import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.installer.output.InstallResult;

import java.util.List;

/**
 * Interface for installing plugin to the jar file.
 */

public interface InstallBoundary {

    /**
     * Install a plugin
     *
     * @param installInput Options
     */
    List<InstallResult> installPlugin(InstallInput installInput);
}
