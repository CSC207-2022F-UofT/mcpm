package org.hydev.mcpm.client.display.presenters;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.hydev.mcpm.client.commands.presenters.InfoPresenter;
import org.hydev.mcpm.client.models.PluginYml;
import org.hydev.mcpm.utils.ColorLogger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Implementation of InfoPresenter
 */
public class KvInfoPresenter implements InfoPresenter
{
    private static final int LEN_LEFT = 12;
    private static final int LEN_RIGHT = 60;
    private static final String KV_PRE = "&7> &f";
    private static final String KV_SEP = " : &6";
    private static final int LEN_INDENT = LEN_LEFT + ColorLogger.lengthNoColor(KV_PRE + KV_SEP);

    /**
     * Format a kv pair if value isn't null, return empty string if value is null
     * <p>
     * Deprecation warnings are suppressed because we don't want to import Apache Commons Text just for wrapping words
     * when Apache Commons Lang3 already provides the WorldUtils (through deprecated).
     *
     * @param key Key
     * @param rawValue Value
     * @return Formatted pair
     */
    @SuppressWarnings("deprecation")
    private static String formatPair(String key, @Nullable Object rawValue)
    {
        if (rawValue == null) return "";

        // If a map is passed in, only show its keys
        if (rawValue instanceof Map<?, ?> v)
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

    @Override
    public void present(PluginYml pl, Consumer<String> log)
    {
        String msg = "&bPlugin Info:\n" +
            formatPair("Name", "&b" + pl.name()) +
            formatPair("Main", pl.main()) +
            formatPair("Version", pl.version()) +
            formatPair("Description", pl.description()) +
            formatPair("Author", pl.author()) +
            formatPair("Authors", pl.authors()) +
            formatPair("Website", pl.website()) +
            formatPair("API Version", pl.apiVersion()) +
            formatPair("Depend", pl.depend()) +
            formatPair("Soft Depend", pl.softdepend()) +
            formatPair("Load Before", pl.loadbefore()) +
            formatPair("Libraries", pl.libraries()) +
            formatPair("Commands", pl.commands());
        log.accept(msg);
    }
}
