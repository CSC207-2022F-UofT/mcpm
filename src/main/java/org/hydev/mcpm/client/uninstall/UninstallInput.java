package org.hydev.mcpm.client.uninstall;

/**
 * Uninstall Plugin input
 *
 * @param name Plugin name
 * @param recursive remove dependencies or not
 * @param delete Whether to delete the file after unloading
 */
public record UninstallInput(
    String name,
    boolean recursive,
    boolean delete
)
{
    /**
     * Default constructor with delete=true
     *
     * @param name Plugin name
     * @param recursive remove dependencies or not
     */
    public UninstallInput(String name, boolean recursive)
    {
        this(name, recursive, true);
    }
}


