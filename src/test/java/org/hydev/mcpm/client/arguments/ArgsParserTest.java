package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.hydev.mcpm.client.arguments.parsers.CommandParser;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.hydev.mcpm.utils.ColorLogger.printc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the ArgsParser class (whether it can handle parsing basic commands, etc).
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

    /**
     * Tests whether the argument parser can parse basic strings for a test command.
     */
    @Test
    void testParse() throws ArgumentParserException
    {
        Consumer<String> out = System.out::println;
        var p = new ArgsParser(List.of(new TestCommand()));
        p.parse(new String[]{"test", "meow"}, out);
        // Should print help
        p.parse(new String[]{}, out);
        // Should print test help
        p.parse(new String[]{"test", "-h"}, out);
        assertThrows(AssertionError.class, () -> p.parse(new String[]{"test", "asd"}, out));
        printc(p.getHelp());
        assertEquals(p.getRawSubparsers().size(), 1);
    }

    /**
     * Tests whether the argument parser will fail on invalid invocations.
     */
    @Test
    void testFail()
    {
        Consumer<String> out = System.out::println;

        var p = new ArgsParser(List.of(new TestCommand()));

        assertThrows(ArgumentParserException.class, () -> p.parse(new String[]{"a"}, out));
    }
}
