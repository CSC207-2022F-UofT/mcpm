package org.hydev.mcpm.client.installer.output;

/**
 * Exception during installation of a plugin
 *
 * @param type Status of install result
 * @param name Name of the plugin
 * @param loaded Whether the installed plugin is successfully loaded
 * @since 2022-11-20
 */
public record InstallResult(Type type, String name, boolean loaded) {

    /**
     * Default constructor with loaded=false
     *
     * @param type Status of install result
     * @param name Name of the plugin
     */
    public InstallResult(Type type, String name) {
        this(type, name, false);
    }

    /**
     * Status of install result
     */
    public enum Type {
        NOT_FOUND(" &c The plugin by that identifier is not found"),
        SEARCH_INVALID_INPUT("  &cInvalid search input"),
        SEARCH_FAILED_TO_FETCH_DATABASE("   &cFailed to fetch the MCPM database"),
        NO_VERSION_AVAILABLE("  &cNo versions are available to download"),
        PLUGIN_EXISTS(" &6The plugin is already installed on the system"),

        SUCCESS_INSTALLED(" &aThe plugin is installed successfully"),

        UNLOADED(" &bThe plugin is unloaded"),

        LOADED(" &bThe plugin is loaded");
        private final String reason;

        Type(String reason) {
            this.reason = reason;
        }

        public String reason() {
            return reason;
        }
    }
}
