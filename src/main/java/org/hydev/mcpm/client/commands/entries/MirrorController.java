package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.database.mirrors.MirrorSelectBoundary;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import static org.hydev.mcpm.utils.FormatUtils.tabulate;

/**
 * Controller for the mirror selection command
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-22
 */
public record MirrorController(MirrorSelectBoundary boundary)
{
    /**
     * Format ping with colors
     *
     * @param ping Ping in milliseconds
     * @return Ping with colors
     */
    private static String formatPing(long ping)
    {
        var color = ping <= 100 ? "&a" : ping <= 200 ? "&e" : "&c";
        return color + ping + " ms";
    }

    /**
     * Format speed with colors
     *
     * @param speed Speed in mbps
     * @return Speed with colors
     */
    private static String formatSpeed(long speed)
    {
        var color = speed >= 750 ? "&a" : speed >= 500 ? "&e" : "&c";
        return color + speed + " mbps";
    }

    /**
     * Ask the user to select a mirror
     *
     * @param refresh If we should refresh the database
     * @param log Logger
     */
    public void ping(boolean refresh, Consumer<String> log)
    {
        try
        {
            if (refresh) boundary.updateMirrors();
            var selected = boundary.getSelectedMirror();

            // Display the top 20 results
            var ping = boundary.pingMirrors();
            log.accept(tabulate(ping.stream().limit(20).map(it ->
                    List.of((selected.host().equals(it.k().host()) ? "&6> " : "&f  ") + it.k().host(),
                        formatPing(it.v()), formatSpeed(it.k().speed()))).toList(),
                List.of(":Host", "Delay:", "Speed:")));
        }
        catch (IOException e)
        {
            log.accept(String.format("&cUnexpected error during processing: %s", e));
        }
    }

    /**
     * Select a mirror
     *
     * @param host Hostname of the mirror
     * @param log Logger
     */
    public void select(String host, Consumer<String> log)
    {
        try
        {
            var mirror = boundary.listAvailableMirrors().stream().filter(it -> it.host().equals(host)).findFirst()
                .orElseThrow(() -> new AssertionError(String.format("No mirror of the host %s is found", host)));

            boundary.setSelectedMirror(mirror);
            log.accept(String.format("&aSuccessfully selected %s as the mirror source", host));
        }
        catch (IOException e)
        {
            log.accept(String.format("&cError occurred while writing the configuration file: %s", e));
        }
        catch (AssertionError e)
        {
            log.accept("&c" + e.getMessage());
        }
    }
}
