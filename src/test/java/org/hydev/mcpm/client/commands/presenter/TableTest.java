package org.hydev.mcpm.client.commands.presenter;

import org.hydev.mcpm.client.commands.presenter.Table;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hydev.mcpm.utils.ColorLogger.printc;

/**
 * Tests for FormatUtils
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-20
 */
class TableTest
{
    @Test
    void tabulate()
    {
        printc(Table.tabulate(List.of(
            List.of("Azalea", "meow", "Cakes without eggs or sugar"),
            List.of("Lindsey", "meow", "Burgers")
        ), List.of(":Contributor", "Centered", "Favorite Food:")));
    }
}
