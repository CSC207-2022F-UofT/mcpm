package org.hydev.mcpm.client.commands.entries;

import com.google.common.collect.Streams;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.hydev.mcpm.client.database.PluginTracker;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Controller for the info use case
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-22
 */
public record InfoController(PluginTracker tracker)
{
    private static final int LEN_LEFT = 12;
    private static final int LEN_RIGHT = 60;

    /**
     * Format a kv pair if value isn't null, return empty string if value is null
     *
     * @param key Key
     * @param rawValue Value
     * @return Formatted pair
     */
    private static String formatPair(String key, @Nullable Object rawValue)
    {
        if (rawValue == null) return "";

        String value;
        if (rawValue instanceof Map<?,?> v) rawValue = v.keySet().stream().toList();
        if (rawValue instanceof List<?> v) value = String.join(", ", v.stream().map(Object::toString).toList());
        else value = rawValue.toString();

        // Wrap text
        value = String.join("\n", Arrays.stream(WordUtils.wrap(value, LEN_RIGHT).split("\n"))
            .map(it -> StringUtils.repeat(" ", LEN_LEFT + 5) + "&6" + it).toList()).strip();

        return "&7> &f" + StringUtils.rightPad(key, LEN_LEFT) + " : &6" + value + "&r\n";
    }

    /**
     * Run the info command
     *
     * @param name Name of the plugin
     * @param log Logger
     */
    public void info(String name, Consumer<String> log)
    {
        try
        {
            var pl = tracker.listInstalled().stream().filter(it -> it.name().equalsIgnoreCase(name)).findFirst()
                .orElseThrow(() -> new AssertionError(String.format("Cannot find plugin '%s'", name)));

            var msg = "&bPlugin Info:\n";
            msg += formatPair("Name", "&b" + pl.name());
            msg += formatPair("Main", pl.main());
            msg += formatPair("Version", pl.version());
            msg += formatPair("Description", pl.description());
            msg += formatPair("Author", pl.author());
            msg += formatPair("Authors", pl.authors());
            msg += formatPair("Website", pl.website());
            msg += formatPair("API Version", pl.apiVersion());
            msg += formatPair("Depend", pl.depend());
            msg += formatPair("Soft Depend", pl.softdepend());
            msg += formatPair("Load Before", pl.loadbefore());
            msg += formatPair("Libraries", pl.libraries());
            msg += formatPair("Commands", pl.commands());
            log.accept(msg);
        }
        catch (AssertionError e)
        {
            log.accept("&c" + e.getMessage());
        }
    }
}
