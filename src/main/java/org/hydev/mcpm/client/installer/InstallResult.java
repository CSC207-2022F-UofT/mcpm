package org.hydev.mcpm.client.installer;

import org.hydev.mcpm.client.models.PluginModel;

import java.util.Collection;

/**
 * Exception during installation of a plugin
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @author Rena (https://github.com/thudoan1706)
 * @since 2022-11-20
 */
public record InstallResult(Type type) {
    /**
     * Type of install failure
     */
    public enum Type {
        NOT_FOUND("Plugin by that identifier is not found"),
        SEARCH_INVALID_INPUT("Invalid search input"),
        SEARCH_FAILED_TO_FETCH_DATABASE("Failed to fetch the MCPM database"),
        NO_VERSION_AVAILABLE("No versions are available to download"),
        PLUGIN_EXISTS("The plugin is already installed on the system"),

        SUCCESS_INSTALLED_AND_FAIL_LOADED("Plugin is installed successfully, but fail to be loaded"),

        SUCCESS_INSTALLED_AND_LOADED("Plugin are installed successfully and loaded"),

        SUCCESS_INSTALLED_AND_UNLOADED("Plugin are installed successfully and unloaded");

        private final String reason;

        Type(String reason) {
            this.reason = reason;
        }

        public String reason() {
            return reason;
        }
    }
}
