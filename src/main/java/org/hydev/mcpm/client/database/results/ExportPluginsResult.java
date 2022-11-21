package org.hydev.mcpm.client.database.results;

/**
 * Results returned from ExportPluginBoundary.
 *
 * @param state The outcome of the export. Must be SUCCESS for other values to be valid.
 * @author Peter (https://github.com/MstrPikachu)
 * @since 2022-11-19
 */
public record ExportPluginsResult(
        State state
) {
    /**
     * Outcome of the plugin export
     */
    public enum State {
        SUCCESS,
        FAILED_TO_FETCH_PLUGINS
    }
}
