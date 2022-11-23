package org.hydev.mcpm.client.commands.entries;

import com.google.common.collect.Streams;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.hydev.mcpm.client.database.PluginTracker;
import org.hydev.mcpm.utils.ColorLogger;

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
    private static final String KV_PRE = "&7> &f";
    private static final String KV_SEP = " : &6";
    private static final int LEN_INDENT = LEN_LEFT + ColorLogger.lengthNoColor(KV_PRE + KV_SEP);

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

        // If a map is passed in, only show its keys
        if (rawValue instanceof Map<?,?> v)
        {
            rawValue = v.keySet().stream().toList();
        }

        String value;

        if (rawValue instanceof List<?> v)
        {
            // If a list is passed in, join it with commas.
            value = String.join(", ", v.stream().map(Object::toString).toList());
        }
        else
        {
            // If any other types of object is passed in, just make it string
            value = rawValue.toString();
        }

        // Wrap text
        value = String.join("\n", Arrays.stream(WordUtils.wrap(value, LEN_RIGHT).split("\n"))
            .map(it -> StringUtils.repeat(" ", LEN_INDENT) + "&6" + it).toList()).strip();

        return KV_PRE + StringUtils.rightPad(key, LEN_LEFT) + KV_SEP + value + "&r\n";
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
