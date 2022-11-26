package org.hydev.mcpm.client.uninstallUseCase;

import org.hydev.mcpm.client.injector.PluginLoader;
import org.hydev.mcpm.client.injector.UnloadBoundary;

/**
 * Uninstall Plugin input
 *
 * @param name Plugin name
 */
public  record UninstallInput(String name,
                              PluginLoader ulb,
                              SearchPackagesType spType,
                              boolean remove_depen){

}



