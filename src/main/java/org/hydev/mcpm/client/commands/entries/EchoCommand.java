package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.Command;

import java.util.function.Consumer;

/**
 * Demo "echo" what the input is command.
 * See EchoEntry and EchoParser.
 */
public class EchoCommand implements Command<EchoEntry> {
    @Override
    public Class<EchoEntry> type() {
        return EchoEntry.class;
    }

    @Override
    public void run(EchoEntry input, Consumer<String> log) {
        log.accept("Echo: " + input.text());
    }
}

