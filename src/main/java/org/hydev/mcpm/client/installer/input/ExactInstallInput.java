package org.hydev.mcpm.client.installer.input;

import org.hydev.mcpm.client.models.PluginModel;

/**
 * Install input that provides an exact plugin model class
 */
public record ExactInstallInput(
    PluginModel model,
    boolean load,
    boolean isManuallyInstalled

) implements InstallInput { }
