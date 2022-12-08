package org.hydev.mcpm.client.installer;


import org.hydev.mcpm.client.installer.input.InstallInput;
import org.hydev.mcpm.client.installer.output.InstallResult;
import org.hydev.mcpm.client.models.PluginModel;

import java.util.List;

/**
 * Interface for installing plugin to the jar file.
 */

public interface InstallBoundary {

    /**
     * Install a plugin
     *
     * @param installInput Options
     * @return Install results
     */
    List<InstallResult> installPlugin(InstallInput installInput);

    /**
     * Install an exact plugin using a plugin model
     *
     * @param model Plugin model
     * @return Install results
     */
    List<InstallResult> installExact(PluginModel model);
}
