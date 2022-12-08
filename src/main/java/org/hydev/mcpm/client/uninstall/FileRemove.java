package org.hydev.mcpm.client.uninstall;

/**
 * Interface for removing a file
 */

public interface FileRemove {
    /**
     * Remove file for given plugin (step 2 and 3 of Uninstaller use case)
     *
     * @param pluginName Name of the plugin to remove
     * @return int 0 - true, 1 - NOT_FOUND, 2 - FAILED_TO_DELETE
     */
    FileRemoveResult removeFile(String pluginName);
}
