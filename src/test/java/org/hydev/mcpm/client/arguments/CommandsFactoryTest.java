package org.hydev.mcpm.client.arguments;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the CommandFactory
 */
class CommandsFactoryTest
{
    @Test
    void test()
    {
        var ba = CommandsFactory.baseArgsParser();

        assertTrue(ba.getRawSubparsers().stream().anyMatch(it -> it.name().equals("list")));
        assertTrue(ba.getRawSubparsers().stream().noneMatch(it -> it.name().equals("load")));
    }
}
