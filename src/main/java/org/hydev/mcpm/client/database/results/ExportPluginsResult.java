package org.hydev.mcpm.client.database.results;

/**
 * Results returned from ExportPluginBoundary.
 *
 * @param state The outcome of the export. Must be SUCCESS for other values to be valid.
 */
public record ExportPluginsResult(
        State state
) {
    /**
     * Outcome of the result
     */
    public enum State {
        SUCCESS,
        FAILED_TO_FETCH_PLUGINS
    }
}
