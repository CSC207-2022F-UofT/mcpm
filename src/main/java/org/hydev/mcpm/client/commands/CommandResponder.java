package org.hydev.mcpm.client.commands;

import java.util.function.Consumer;

public interface CommandResponder {
    boolean handles(CommandEntry input);
    void run(CommandEntry input, Consumer<String> log);
}
