package org.hydev.mcpm.client.display.presenters

import org.hydev.mcpm.client.commands.presenters.ListResultPresenter
import org.hydev.mcpm.client.display.presenters.Table.Companion.tabulate
import org.hydev.mcpm.client.interaction.ILogger
import org.hydev.mcpm.client.list.ListResult

/**
 * Present list result
 *
 * @param log log string to the console
 */
class ListPresenter(val log: ILogger) : ListResultPresenter
{
    override fun displayResult(res: ListResult)
    {
        when (res.type)
        {
            ListResult.Type.SEARCH_INVALID_INPUT ->
            {
                log.print("${res.type.reason()}\n" +
                    "Invalid input. Please enter one of the following: all, manual, automatic, outdated")
            }
            ListResult.Type.SEARCH_FAILED_TO_FETCH_INSTALLED ->
            {
                log.print("${res.type.reason()}\n" +
                    "Unable to fetch result.")
            }
            else ->
            {
                val table = tabulate(res.queryResult.map { listOf("&a" + it.name, "&e" + it.firstAuthor, it.version) },
                    listOf(":Name", "Author", "Version:"))
                log.print("${res.type.reason()}\n" +
                    "(Parameter ${res.listType})\n$table")
            }
        }
    }
}
