package org.hydev.mcpm.client.database;

import org.hydev.mcpm.client.models.Plugin;
import org.hydev.mcpm.client.models.PluginVersion;

import java.io.File;
import java.util.List;

/**
 * This class keeps track of locally installed packages
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-09-27
 */
public class LocalPluginTracker
{
    /**
     * Read metadata from a plugin's jar
     *
     * @param jar Local plugin jar path
     * @return Metadata
     */
    public PluginVersion readMeta(File jar)
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * List all currently installed plugins
     */
    public PluginVersion listInstalled()
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Mark a plugin as manually installed (as opposed to a dependency)
     *
     * @param name Plugin name
     */
    public void addManuallyInstalled(String name)
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Remove a plugin from the manually installed plugin list
     *
     * @param name Plugin name
     */
    public void removeManuallyInstalled(String name)
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Get a list of manually installed plugins
     *
     * @return List of plugin names
     */
    public List<String> listManuallyInstalled()
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Get a list of automatically installed plugin dependencies that are no longer required
     *
     * @return List of plugin names
     */
    public List<String> listOrphans()
    {
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }
}
