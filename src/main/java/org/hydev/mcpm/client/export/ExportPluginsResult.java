package org.hydev.mcpm.client.export;

import org.jetbrains.annotations.Nullable;

/**
 * Results returned from ExportPluginBoundary.
 *
 * @param state The outcome of the export. Must be SUCCESS for other values to be valid.
 * @param export A string representing where the export went to.
 */
public record ExportPluginsResult(
        State state,
        @Nullable String export
) {
    /**
     * Outcome of the plugin export
     */
    public enum State {
        SUCCESS,
        FAILED_TO_FETCH_PLUGINS
    }
}
