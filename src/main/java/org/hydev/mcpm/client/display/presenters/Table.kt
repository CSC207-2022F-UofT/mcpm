package org.hydev.mcpm.client.display.presenters

import org.apache.commons.lang3.StringUtils
import org.hydev.mcpm.client.commands.presenters.PagedPresenter
import org.hydev.mcpm.utils.ColorLogger.lengthNoColor
import kotlin.math.ceil

/**
 * Utility functions for formatting the CLI
 */
data class Table(val headers: List<String>, val rows: List<List<String>>, val sep: String = " | ") : PagedPresenter<Table>
{
    override fun toString() = tabulate(rows, headers, sep)

    override fun presentPage(page: Int, lines: Int): Table
    {
        val pg = rows.stream().skip((page - 1).toLong() * lines).limit(lines.toLong()).toList()
        return Table(headers, pg, sep)
    }

    override fun total(lines: Int) = ceil(rows.size * 1.0 / lines).toInt()

    /**
     * String justification type
     */
    private enum class Justify { LEFT, RIGHT, CENTER }

    companion object
    {
        /**
         * Justify a string
         *
         * @param src Input string
         * @param method Justification Method
         * @param len Justification length
         * @return Justified string
         */
        private fun justify(src: String, method: Justify, len: Int): String
        {
            // Adjust for color
            val len = len + src.length - lengthNoColor(src)

            // Justify
            return when (method)
            {
                Justify.LEFT -> StringUtils.rightPad(src, len)
                Justify.RIGHT -> StringUtils.leftPad(src, len)
                Justify.CENTER -> StringUtils.center(src, len)
            }
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
        @JvmOverloads
        fun tabulate(rows1: List<List<String>>, headers: List<String>, sep: String = "&r | "): String
        {
            // Make rows mutable
            val rows = rows1.toMutableList()

            // Find out justify method for each column
            val justify = headers.map {
                if (it.endsWith(":")) Justify.RIGHT else if (it.startsWith(":")) Justify.LEFT else Justify.CENTER
            }

            // Add headers row as a row, bold headers
            rows.add(0, headers.map { "&f&n${it.trim(':')}&r" })

            // Find max lengths for each column
            val lens = headers.indices.map { col: Int -> rows.maxOfOrNull { lengthNoColor(it[col]) } ?: 0 }

            // Format string
            val lines = rows.map { it.mapIndexed { col, v -> justify(v, justify[col], lens[col]) }.joinToString(sep) }

            // Join
            return lines.joinToString("&r\n")
        }
    }
}
