package org.hydev.mcpm.client.commands.presenter;

import com.google.common.collect.Streams;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.StringUtils.center;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.StringUtils.rightPad;
import static org.hydev.mcpm.utils.ColorLogger.lengthNoColor;

/**
 * Utility functions for formatting the CLI
 */
public record Table(List<String> headers, List<List<String>> rows, String sep) implements PagedPresenter<Table>
{
    /**
     * Table with default separator
     *
     * @param headers Headers (see tabulate())
     * @param rows Rows (see tabulate())
     */
    public Table(List<String> headers, List<List<String>> rows)
    {
        this(headers, rows, " | ");
    }

    @Override
    public String toString()
    {
        return tabulate(rows, headers, sep);
    }

    @Override
    public Table presentPage(int page, int lines)
    {
        page -= 1;
        var pg = rows.stream().skip((long) page * lines).limit(lines).toList();
        return new Table(headers, pg, sep);
    }

    @Override
    public int total(int lines)
    {
        return (int) Math.ceil(rows.size() * 1.0 / lines);
    }

    /**
     * String justification type
     */
    private enum Justify
    {
        LEFT, RIGHT, CENTER
    }

    /**
     * Justify a string
     *
     * @param in Input string
     * @param method Justification Method
     * @param len Justification length
     * @return Justified string
     */
    private static String justify(String in, Justify method, int len)
    {
        // Adjust for color
        len += in.length() - lengthNoColor(in);

        // Justify
        return switch (method)
        {
            case LEFT -> rightPad(in, len);
            case RIGHT -> leftPad(in, len);
            case CENTER -> center(in, len);
        };
    }

    /**
     * Tabulate a table, with justify and adjusted for colors. This function processes justification automatically. If
     * a header begins with :, then it is justified to the left. If a header ends with :, then it is justified to the
     * right.
     *
     * @param rows1 Rows of objects (should have length R, with each row having length C)
     * @param headers Headers (should have length C)
     * @return Formatted string
     */
    public static String tabulate(List<List<String>> rows1, List<String> headers)
    {
        return tabulate(rows1, headers, "&r | ");
    }

    /**
     * Tabulate a table, with justify and adjusted for colors. This function processes justification automatically. If
     * a header begins with :, then it is justified to the left. If a header ends with :, then it is justified to the
     * right.
     *
     * @param rows1 Rows of objects (should have length R, with each row having length C)
     * @param headers Headers (should have length C)
     * @param sep Separator
     * @return Formatted string
     */
    public static String tabulate(List<List<String>> rows1, List<String> headers, String sep)
    {
        // Make rows mutable
        var rows = new ArrayList<>(rows1);

        // Find out justify method for each column
        var justify = headers.stream().map(h -> h.endsWith(":") ? Justify.RIGHT
            : h.startsWith(":") ? Justify.LEFT : Justify.CENTER).toList();

        // Add headers row as a row, bold headers
        rows.add(0, headers.stream().map(h -> "&f&n" + StringUtils.strip(h, ":") + "&r").toList());

        // Find max lengths for each column
        var lens = IntStream.range(0, headers.size()).map(col -> rows.stream()
            .mapToInt(r -> lengthNoColor(r.get(col))).max().orElse(0)).toArray();

        // Format string
        var lines = rows.stream().map(row -> String.join(sep, Streams.mapWithIndex(row.stream(),
            (v, col) -> justify(v, justify.get((int) col), lens[(int) col])).toList())).toList();

        // Join
        return String.join("&r\n", lines);
    }
}

