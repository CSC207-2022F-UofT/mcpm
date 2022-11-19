package org.hydev.mcpm.client.database.results;

public record ExportPluginsResult(
        State state
) {
    public enum State{
        SUCCESS,
        FAILED_TO_FETCH_DATABASE
    }
}
