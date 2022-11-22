package org.hydev.mcpm.client.arguments;

import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests for the CommandFactory
 *
 * @author Azalea (https://github.com/hykilpikonna)
 * @since 2022-11-21
 */
class CommandsFactoryTest
{
    @Test
    void test() throws ArgumentParserException
    {
        var ba = CommandsFactory.baseArgsParser();
        var sa = CommandsFactory.serverArgsParser(System.out::println);

        assert ba.getRawSubparsers().stream().anyMatch(it -> it.name().equals("list"));
        assert ba.getRawSubparsers().stream().noneMatch(it -> it.name().equals("load"));
        assert sa.getRawSubparsers().stream().anyMatch(it -> it.name().equals("list"));
        assert sa.getRawSubparsers().stream().anyMatch(it -> it.name().equals("load"));

        AtomicReference<String> log = new AtomicReference<>("");
        ba.parse(new String[]{"echo", "hi"}, s -> log.set(log.get() + s));
        assert log.get().equals("Echo: hi");
    }
}
