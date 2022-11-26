package org.hydev.mcpm.client.uninstall;

import org.hydev.mcpm.client.database.inputs.SearchPackagesType;
import org.hydev.mcpm.client.injector.PluginLoader;

/**
 * Uninstall Plugin input
 *
 * @param name Plugin name
 * @param ulb PluginLoader object
 * @param spType SearchPackagesType
 * @param recursive remove dependencies or not
 */
public  record UninstallInput(
    String name,
    PluginLoader ulb,
    SearchPackagesType spType,
    boolean recursive
) { }


