package org.hydev.mcpm.client.installer;

/**
 * Exception during installation of a plugin
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @author Rena (https://github.com/thudoan1706)
 * @since 2022-11-20
 */
public class InstallException extends Exception
{
    private final Type type;

    public InstallException(Type type)
    {
        this.type = type;
    }

    /**
     * Type of install failure
     */
    public enum Type
    {
        NOT_FOUND("Plugin by that identifier is not found"),
        SEARCH_INVALID_INPUT("Invalid search input"),
        SEARCH_FAILED_TO_FETCH_DATABASE("Failed to fetch the MCPM database"),
        NO_VERSION_AVAILABLE("No versions are available to download"),
        PLUGIN_EXISTS("The plugin is already installed on the system");

        private final String reason;

        Type(String reason) {
            this.reason = reason;
        }

        public String reason()
        {
            return reason;
        }
    }

    public Type type()
    {
        return type;
    }

    @Override
    public String getMessage()
    {
        return type().reason();
    }

    @Override
    public String getLocalizedMessage()
    {
        return getMessage();
    }
}
