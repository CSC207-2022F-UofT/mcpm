package org.hydev.mcpm.client.uninstallUseCase;

import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.injector.UnloadBoundary;

/**
 * Uninstall Plugin input
 *
 * @param name Plugin name
 * @param ulb PluginLoader object
 * @param spType SearchPackagesType
 * @param remove_depen remove dependencies or not
 */
public  record UninstallInput(String name,
                              PluginLoader ulb, SearchPackagesType spType, boolean remove_depen){

}



