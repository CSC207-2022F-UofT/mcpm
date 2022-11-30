package org.hydev.mcpm.client.installer.input;

import org.hydev.mcpm.client.search.SearchPackagesType;

/**
 * Install Plugin Input
 *
 * @param name     Plugin name from repository
 * @param type     Search packages type (BY_NAME, BY_KEYWORD, BY_COMMAND)
 * @param load     Whether to load after installing
 * @param isManuallyInstalled Whether the user already asked installing the plugin
 */
public record InstallInput(String name,
         SearchPackagesType type,
         boolean load,
         boolean isManuallyInstalled) {

}

