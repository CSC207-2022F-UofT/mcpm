package org.hydev.mcpm.client.uninstall;

/**
 * Result for uninstalling
 *
 * @author Anushka (https://github.com/aanushkasharma)
 * @since 2022-11-27
 */
public record UninstallResult(State state)
{
    public enum State {
        FAILED_TO_DELETE,
        SUCCESS
    }
}
