package org.hydev.mcpm.client.installer.input;

/**
 * TODO: Write a description for this class!
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-12-08
 */
public interface InstallInput
{
    /**
     * Returns the value of the final field "load"
     *
     * @return Whether the plugin should be loaded after installing
     */
    boolean load();

    /**
     * Returns the value of the final field "load"
     *
     * @return Whether the plugin should be marked as manually installed.
     */
    boolean isManuallyInstalled();
}
