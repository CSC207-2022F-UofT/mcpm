package org.hydev.mcpm.client.updater;

/**
 * Boundary for the update command.
 */
public interface UpdateBoundary {
    /**
     * Request an update for the plugins in UpdateInput.
     *
     * @param input An input containing information on the plugins to update.
     * @return A result detailing which plugins were updated.
     */
    UpdateResult update(UpdateInput input);
}
