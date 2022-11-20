package org.hydev.mcpm.client.commands.entries;

import java.util.function.Consumer;

/**
 * Demo "echo" what the input is command.
 * See EchoParser.
 */
public class EchoController {
    public void echo(String text, Consumer<String> log) {
        log.accept("Echo: " + text);
    }
}

