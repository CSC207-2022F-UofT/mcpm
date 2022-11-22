package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.database.MirrorSelectBoundary;

import java.util.function.Consumer;

/**
 * Controller for the mirror selection command
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-22
 */
public record MirrorController(MirrorSelectBoundary boundary)
{
    /**
     * Ask the user to select a mirror
     *
     * @param refresh If we should refresh the database
     * @param log Logger
     */
    public void ping(boolean refresh, Consumer<String> log)
    {
        System.out.println("ping");
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Select a mirror
     *
     * @param host Hostname of the mirror
     * @param log Logger
     */
    public void sel(String host, Consumer<String> log)
    {
        System.out.println("sel");
        // TODO: Implement this
        throw new UnsupportedOperationException("TODO");
    }
}
