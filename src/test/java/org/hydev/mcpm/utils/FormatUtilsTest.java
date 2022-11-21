package org.hydev.mcpm.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hydev.mcpm.utils.ColorLogger.printc;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for FormatUtils
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-20
 */
class FormatUtilsTest
{
    @Test
    void tabulate()
    {
        printc(FormatUtils.tabulate(List.of(
            List.of("Azalea", "meow", "Cakes without eggs or sugar"),
            List.of("Lindsey", "meow", "Burgers")
        ), List.of(":Contributor", "Centered", "Favorite Food:")));
    }
}
