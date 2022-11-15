package org.hydev.mcpm.client.commands.entries;

import org.hydev.mcpm.client.commands.Command;

public class EchoCommand implements Command<EchoEntry> {
    @Override
    public Class<EchoEntry> type() {
        return EchoEntry.class;
    }

    @Override
    public void run(EchoEntry input) {
        System.out.println("Echo: " + input.text());
    }
}

