package org.hydev.mcpm.client.updater;

import org.jetbrains.annotations.Nullable;

/**
 * Represents an outcome from an Update request (was the plugin updated? etc.).
 *
 * @param state What happened to the plugin (updated, ignored, ...)
 * @param initialVersion The currently installed/known version of the plugin.
 * @param destinationVersion The version that the plugin was updated to (null state != SUCCESS).
 */
public record UpdateOutcome(
    State state,
    @Nullable String initialVersion,
    @Nullable String destinationVersion
) {
    /**
     * Represents what was done to the plugin during an update.
     */
    public enum State {
        /**
         * Set when a plugin was not found in the database, so it was not updated.
         */
        MISMATCHED,
        /**
         * Set when this plugin was requested to be updated, but could not be found on the system.
         */
        NOT_INSTALLED,
        /**
         * Set when something went wrong related to fetching plugin information.
         */
        NETWORK_ERROR,
        /**
         * Set when the version of the plugin installed is the same as the one in the database.
         * Use --no-cache to force a update of the database.
         */
        UP_TO_DATE,
        /**
         * Set when the plugin was out of date, so it was updated to the latest version.
         * The version that it was updated to is in destinationVersion.
         */
        UPDATED;

        /**
         * Returns true in success states.
         *
         * @return True if this is either SUCCESS or UP_TO_DATE.
         */
        public boolean success() {
            return switch (this) {
                case MISMATCHED, NOT_INSTALLED, NETWORK_ERROR -> false;
                case UP_TO_DATE, UPDATED -> true;
            };
        }
    }
}
