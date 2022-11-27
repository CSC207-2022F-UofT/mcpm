package org.hydev.mcpm.client.uninstall;

/**
 * Uninstall Plugin input
 *
 * @param name Plugin name
 * @param recursive remove dependencies or not
 */
public  record UninstallInput(
    String name,
    boolean recursive
) { }


