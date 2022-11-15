package org.hydev.mcpm.client.commands;

public interface CommandResponder {
    boolean handles(CommandEntry input);
    void run(CommandEntry input);
}
