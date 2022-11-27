package org.hydev.mcpm.client.updater;

import org.jetbrains.annotations.Nullable;

public record UpdateOutcome(
    State state,
    @Nullable String initialVersion,
    @Nullable String destinationVersion
) {
    public enum State {
        MISMATCHED,
        NOT_INSTALLED,
        NETWORK_ERROR,
        UP_TO_DATE,
        UPDATED;

        public boolean success() {
            return switch (this) {
                case MISMATCHED, NOT_INSTALLED, NETWORK_ERROR -> false;
                case UP_TO_DATE, UPDATED -> true;
            };
        }
    }
}
