package org.hydev.mcpm.client.list;


import org.hydev.mcpm.client.models.PluginYml;

import java.util.List;


/**
 * Exception during installation of a plugin
 */
public record ListResult(List<PluginYml> queryResult, Type type) {
    /**
     * Type of list failure
     */
    public enum Type {
        SEARCH_INVALID_INPUT("Invalid search input"),
        SEARCH_FAILED_TO_FETCH_INSTALLED("Failed to fetch the local files"),
        SUCCESS_RETRIEVING_LOCAL_BUT_FAIL_UPDATABLE("Successfully retrieved local files," +
                " but failed to retrieve updatable files"),
        SUCCESS_RETRIEVING_LOCAL_AND_UPDATABLE("Successfully retrieved local files");

        private final String reason;

        Type(String reason) {
            this.reason = reason;
        }

        public String reason() {
            return reason;
        }
    }
}
