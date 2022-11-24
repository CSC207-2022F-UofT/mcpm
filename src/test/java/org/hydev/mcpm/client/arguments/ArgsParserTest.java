package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.arguments.parsers.CommandParser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.hydev.mcpm.utils.ColorLogger.printc;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ArgsParser
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-21
 */
class ArgsParserTest
{
    static class TestCommand implements CommandParser
    {
        @Override
        public void run(Namespace details, Consumer<String> log)
        {
            log.accept(details.toString());
            assertEquals(details.getString("a"), "meow");
        }

        @Override
        public String name()
        {
            return "test";
        }

        @Override
        public String description()
        {
            return "A testing command";
        }

        @Override
        public void configure(Subparser parser)
        {
            parser.addArgument("a");
        }
    }

    @Test
    void parse() throws ArgumentParserException
    {
        var p = new ArgsParser(List.of(new TestCommand()), System.out::println);
        p.parse(new String[]{"test", "meow"});
        // Should print help
        p.parse(new String[]{});
        // Should print test help
        p.parse(new String[]{"test", "-h"});
        assertThrows(AssertionError.class, () -> p.parse(new String[]{"test", "asdf"}));
        printc(p.help());
        assert p.getRawSubparsers().size() == 1;
    }

    @Test
    void fail()
    {
        var p = new ArgsParser(List.of(new TestCommand()), System.out::println);
        try
        {
            p.parse(new String[]{"a"});
        }
        catch (ArgumentParserException e)
        {
            p.fail(e);
        }
    }
}
