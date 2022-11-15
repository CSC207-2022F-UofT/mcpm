package org.hydev.mcpm.client.commands;

public interface Command<T extends CommandEntry> {
    Class<T> type();
    void run(T input);
}
