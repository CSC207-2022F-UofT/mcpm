package org.hydev.mcpm.client.commands.presenter

import org.hydev.mcpm.client.display.presenters.Table
import org.hydev.mcpm.utils.ColorLogger.printc
import org.junit.jupiter.api.Test

/**
 * Tests for FormatUtils
 */
internal class TableTest
{
    @Test
    fun tabulate()
    {
        printc(Table(listOf(":Contributor", "Centered", "Favorite Food:"), listOf(
            listOf("Azalea", "meow", "Cakes without eggs or sugar"),
            listOf("Lindsey", "meow", "Burgers")
        )).toString())
    }
}
