package org.hydev.mcpm.client.commands;

import java.util.function.Consumer;

public interface Command<T extends CommandEntry> {
    Class<T> type();
    void run(T input, Consumer<String> log);
}
