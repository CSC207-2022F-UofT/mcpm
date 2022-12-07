package org.hydev.mcpm.client.uninstall;

import org.hydev.mcpm.client.injector.LocalJarBoundary;
import org.hydev.mcpm.client.injector.PluginNotFoundException;

import java.io.File;

import static org.hydev.mcpm.client.uninstall.UninstallResult.State.FAILED_TO_DELETE;
import static org.hydev.mcpm.client.uninstall.UninstallResult.State.NOT_FOUND;

/**
 * Interface for removing a file
 */

public interface FileRemove {
    /**
     * Remove file for given plugin (step 2 and 3 of Uninstaller use case)
     * @param pluginName
     * @return int 0 - true, 1 - NOT_FOUND, 2 - FAILED_TO_DELETE
     */
    int removeFile(String pluginName);

}
